package qkl.zn.AuctionSystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemGetBidRecordsByItemInputBO;
import qkl.zn.AuctionSystem.pojo.dto.BidDTO;
import qkl.zn.AuctionSystem.result.Result;
import qkl.zn.AuctionSystem.service.AuctionSystemService;
import qkl.zn.AuctionSystem.service.BidService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/bid")
@CrossOrigin
public class BidController {
    @Autowired
    private BidService bidService;
    
    @Autowired
    private AuctionSystemService auctionSystemService;

    @PostMapping
    public Result bidPrice(@RequestBody BidDTO bidDTO){
        log.info("用户出价:{}", bidDTO);
        bidService.bidPrice(bidDTO);
        return Result.success();
    }
    
    /**
     * 查询竞拍记录
     * @param itemId 拍品ID
     * @return 竞拍记录列表
     */
    @GetMapping("/records/{itemId}")
    public Result getBidRecords(@PathVariable Long itemId) {
        log.info("查询竞拍记录: itemId={}", itemId);
        
        try {
            // 构造区块链调用参数
            AuctionSystemGetBidRecordsByItemInputBO input = new AuctionSystemGetBidRecordsByItemInputBO();
            input.set_itemId(BigInteger.valueOf(itemId));
            
            // 调用区块链合约查询竞拍记录
            org.fisco.bcos.sdk.transaction.model.dto.CallResponse response = auctionSystemService.getBidRecordsByItem(input);
            
            // 将结果封装成Map返回
            Map<String, Object> result = new HashMap<>();
            result.put("data", response.getReturnObject());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询竞拍记录失败: itemId={}", itemId, e);
            throw new RuntimeException("查询竞拍记录失败: " + e.getMessage(), e);
        }
    }
}
