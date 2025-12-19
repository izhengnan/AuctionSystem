package qkl.zn.AuctionSystem.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import qkl.zn.AuctionSystem.filter.TokenFilter;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemRecordBidInputBO;
import qkl.zn.AuctionSystem.pojo.dto.BidDTO;
import qkl.zn.AuctionSystem.pojo.entity.Bid;
import qkl.zn.AuctionSystem.pojo.entity.Item;
import qkl.zn.AuctionSystem.service.AuctionSystemService;
import qkl.zn.AuctionSystem.service.BidService;
import qkl.zn.AuctionSystem.service.ItemService;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
public class BidServiceImpl implements BidService {
    @Autowired
    private AuctionSystemService auctionSystemService;
    
    @Autowired
    private ItemService itemService;

    @Override
    public void bidPrice(BidDTO bidDTO) {
        // 获取当前用户ID
        Long userId = TokenFilter.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录或令牌无效");
        }
        
        // 获取拍品信息
        Item item = itemService.selectItemById(bidDTO.getItemId());
        if (item == null) {
            throw new RuntimeException("拍品不存在，ID: " + bidDTO.getItemId());
        }
        
        // 检查拍品状态是否为竞拍中
        if (item.getStatus() != 1) {
            throw new RuntimeException("拍品状态不是竞拍中，当前状态: " + item.getStatus());
        }
        
        // 检查出价是否高于当前最高价
        if (bidDTO.getBidPrice() <= (item.getCurrentMaxPrice() != null ? item.getCurrentMaxPrice() : item.getInitialPrice())) {
            throw new RuntimeException("出价必须高于当前最高价");
        }
        
        // 创建出价记录对象
        Bid bid = new Bid();
        BeanUtils.copyProperties(bidDTO, bid);
        bid.setUserId(userId);
        bid.setBidTime(LocalDateTime.now());
        
        // 构造区块链调用参数
        AuctionSystemRecordBidInputBO input = new AuctionSystemRecordBidInputBO();
        input.set_itemId(BigInteger.valueOf(bidDTO.getItemId()));
        input.set_bidderId(BigInteger.valueOf(userId));
        input.set_bidPrice(BigInteger.valueOf(bidDTO.getBidPrice()));
        input.set_bidTime(BigInteger.valueOf(System.currentTimeMillis() / 1000)); // 转换为秒级时间戳
        
        try {
            // 调用区块链合约记录出价
            auctionSystemService.recordBid(input);
        } catch (Exception e) {
            throw new RuntimeException("区块链出价记录失败: " + e.getMessage(), e);
        }
    }
}