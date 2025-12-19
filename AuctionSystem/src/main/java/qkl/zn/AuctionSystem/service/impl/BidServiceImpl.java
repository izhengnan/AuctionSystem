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
        
        // 获取拍品信息
        Item item = itemService.selectItemById(bidDTO.getItemId());
        
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
            
            // 区块链记录成功后再更新数据库中的最高出价
            itemService.updateCurrentMaxPrice(bidDTO.getItemId(), bidDTO.getBidPrice(), userId);
        } catch (Exception e) {
            throw new RuntimeException("区块链出价记录失败: " + e.getMessage(), e);
        }
    }
}