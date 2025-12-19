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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import qkl.zn.AuctionSystem.pojo.dto.BidRecordDTO;

@Slf4j
@RestController
@RequestMapping("/bid")
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
            // 区块链getBidRecordsByItem返回5个数组: ids, itemIds, bidderIds, bidPrices, bidTimes
            org.fisco.bcos.sdk.transaction.model.dto.CallResponse response = auctionSystemService.getBidRecordsByItem(input);
            
            // 将结果转换为List<BidRecordDTO>
            List<BidRecordDTO> records = new ArrayList<>();
            Object returnObject = response.getReturnObject();
            
            log.info("区块链返回对象类型: {}", returnObject.getClass().getName());
            
            if (returnObject instanceof List) {
                List<?> returnList = (List<?>) returnObject;
                log.info("返回列表大小: {}", returnList.size());
                
                // 区块链返回的是5个数组: [ids数组, itemIds数组, bidderIds数组, bidPrices数组, bidTimes数组]
                if (returnList.size() >= 5) {
                    List<?> ids = (List<?>) returnList.get(0);
                    List<?> itemIds = (List<?>) returnList.get(1);
                    List<?> bidderIds = (List<?>) returnList.get(2);
                    List<?> bidPrices = (List<?>) returnList.get(3);
                    List<?> bidTimes = (List<?>) returnList.get(4);
                    
                    int length = ids.size();
                    log.info("竞拍记录数量: {}", length);
                    
                    // 遍历5个数组，构建BidRecordDTO对象
                    for (int i = 0; i < length; i++) {
                        try {
                            Long bidTime = convertToLong(bidTimes.get(i));
                            Long bidderId = convertToLong(bidderIds.get(i));
                            Long bidPrice = convertToLong(bidPrices.get(i));
                            
                            BidRecordDTO record = new BidRecordDTO();
                            record.setBidTime(bidTime); // 秒级时间戳
                            record.setUserId(bidderId);
                            record.setBidPrice(bidPrice); // 分为单位
                            records.add(record);
                            
                            log.debug("第{}条记录: bidTime={}, bidderId={}, bidPrice={}", i, bidTime, bidderId, bidPrice);
                        } catch (Exception e) {
                            log.warn("转换第{}条竞拍记录失败: {}", i, e.getMessage());
                        }
                    }
                } else {
                    log.warn("返回列表大小不足5: {}", returnList.size());
                }
            } else {
                log.warn("返回对象不是List类型: {}", returnObject.getClass().getName());
            }
            
            // 将结果封装成Map返回
            Map<String, Object> result = new HashMap<>();
            result.put("data", records);
            
            log.info("查询竞拍记录成功: 记录数={}", records.size());
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询竞拍记录失败: itemId={}", itemId, e);
            throw new RuntimeException("查询竞拍记录失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 将对象转换为Long
     */
    private Long convertToLong(Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        return 0L;
    }
}
