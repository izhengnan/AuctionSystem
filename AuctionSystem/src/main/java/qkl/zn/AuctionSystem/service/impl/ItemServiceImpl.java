package qkl.zn.AuctionSystem.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import qkl.zn.AuctionSystem.mapper.ItemMapper;
import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;
import qkl.zn.AuctionSystem.result.PageResult;
import qkl.zn.AuctionSystem.service.ItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void addItem(ItemDTO itemDTO) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO,item);
        item.setCurrentMaxPrice(item.getInitialPrice());
        item.setStatus(0);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        itemMapper.addItem(item);
    }

    @Override
    public PageResult selectItemList(ItemPageQueryDTO itemPageQueryDTO) {
        PageHelper.startPage(itemPageQueryDTO.getPage(),itemPageQueryDTO.getPageSize());
        PageResult pageResult = new PageResult();
        Page<Item> page = itemMapper.selectItemList(itemPageQueryDTO);
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());
        return pageResult;
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
    @Scheduled(cron = "0 * * * * ?")
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
                    }
                }
                // 如果当前时间小于开始时间，保持状态为0（未开始）
            }
        }
        
        log.info("拍品状态更新定时任务执行完成");
    }
}