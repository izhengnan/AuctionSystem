package qkl.zn.AuctionSystem.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qkl.zn.AuctionSystem.mapper.ItemMapper;
import qkl.zn.AuctionSystem.mapper.OrderMapper;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemRecordOrderInputBO;
import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;
import qkl.zn.AuctionSystem.pojo.entity.Order;
import qkl.zn.AuctionSystem.result.PageResult;
import qkl.zn.AuctionSystem.service.AuctionSystemService;
import qkl.zn.AuctionSystem.service.ItemService;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private AuctionSystemService auctionSystemService;

    @Override
    public void addItem(ItemDTO itemDTO) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO, item);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        item.setStatus(0); // 默认状态为0（未开始）
        itemMapper.addItem(item);
    }

    @Override
    public PageResult selectItemList(ItemPageQueryDTO itemPageQueryDTO) {
        PageHelper.startPage(itemPageQueryDTO.getPage(), itemPageQueryDTO.getPageSize());
        Page<Item> page = itemMapper.selectItemList(itemPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Item selectItemById(Long id) {
        Item item = itemMapper.selectItemById(id);
        return item;
    }

    @Override
    public void deleteItemByIds(ArrayList<Long> id) {
        itemMapper.deleteItemByIds(id);
    }

    @Override
    public void updateItem(ItemDTO itemDTO) {
        Item item = itemMapper.selectItemById(itemDTO.getId());
        if(item.getStatus() == 0) {
            BeanUtils.copyProperties(itemDTO, item);
            item.setUpdateTime(LocalDateTime.now());
            itemMapper.updateItem(itemDTO);
        }else{
            throw new RuntimeException("该拍品已开始拍卖或已结束拍卖，不可修改");
        }
    }
    
    @Override
    public List<Item> getAllItems() {
        return itemMapper.selectAllItems();
    }
    
    @Override
    public void updateItemStatus(Long id, Integer status) {
        itemMapper.updateItemStatus(id, status);
    }
    
    /**
     * 定时任务：每分钟检查拍品状态并更新
     * 状态规则：
     * 0 - 未开始
     * 1 - 竞拍中
     * 2 - 已结束
     */
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 * * * * ?")
    public void updateItemStatusScheduled() {
        log.info("开始执行拍品状态更新定时任务");
        
        // 获取所有拍品
        List<Item> items = getAllItems();
        LocalDateTime now = LocalDateTime.now();
        
        for (Item item : items) {
            // 只处理状态为0（未开始）或1（竞拍中）的拍品
            if (item.getStatus() == 0 || item.getStatus() == 1) {
                // 如果当前时间在拍品开始时间与结束时间之间，则将拍品状态改为1（竞拍中）
                if (now.isAfter(item.getStartTime()) && now.isBefore(item.getEndTime())) {
                    if (item.getStatus() != 1) {
                        log.info("更新拍品ID {} 状态为竞拍中", item.getId());
                        updateItemStatus(item.getId(), 1);
                    }
                }
                // 如果当前时间大于结束时间，则将拍品状态改为2（已结束）
                else if (now.isAfter(item.getEndTime())) {
                    if (item.getStatus() != 2) {
                        log.info("更新拍品ID {} 状态为已结束", item.getId());
                        updateItemStatus(item.getId(), 2);
                        
                        // 生成订单
                        generateOrderForItem(item);
                    }
                }
                // 如果当前时间小于开始时间，保持状态为0（未开始）
            }
        }
        
        log.info("拍品状态更新定时任务执行完成");
    }
    
    /**
     * 为拍品生成订单
     * @param item 拍品信息
     */
    private void generateOrderForItem(Item item) {
        try {
            // 检查是否有最高出价用户
            if (item.getCurrentMaxUserId() != null && item.getCurrentMaxPrice() != null) {
                // 有最高出价用户，创建待付款订单
                
                // 生成订单ID（使用时间戳混合买家ID生成）
                long timestamp = System.currentTimeMillis();
                long orderId = timestamp * 100000 + item.getCurrentMaxUserId(); // 避免ID冲突
                
                // 创建订单对象
                Order order = Order.builder()
                        .id(orderId)
                        .itemId(item.getId())
                        .userId(item.getCurrentMaxUserId())
                        .dealPrice(item.getCurrentMaxPrice())
                        .status(0) // 0-待付款
                        .updateTime(LocalDateTime.now())
                        .build();
                
                // 保存到数据库
                orderMapper.insertOrder(order);
                
                // 保存到区块链
                AuctionSystemRecordOrderInputBO input = new AuctionSystemRecordOrderInputBO();
                input.set_itemId(BigInteger.valueOf(item.getId()));
                input.set_buyerId(BigInteger.valueOf(item.getCurrentMaxUserId()));
                input.set_dealPrice(BigInteger.valueOf(item.getCurrentMaxPrice()));
                input.set_updateTime(BigInteger.valueOf(System.currentTimeMillis() / 1000)); // 秒级时间戳
                
                auctionSystemService.recordOrder(input);
                
                log.info("为拍品ID {} 生成订单ID {} 并上链成功", item.getId(), orderId);
            } else {
                // 无最高出价用户，创建流拍订单
                log.info("拍品ID {} 无最高出价用户，创建流拍记录", item.getId());
                // 这里可以根据需求决定是否也需要创建订单记录
            }
        } catch (Exception e) {
            log.error("为拍品ID {} 生成订单失败", item.getId(), e);
        }
    }
}