package qkl.zn.AuctionSystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qkl.zn.AuctionSystem.pojo.dto.BidDTO;
import qkl.zn.AuctionSystem.result.Result;
import qkl.zn.AuctionSystem.service.BidService;

@Slf4j
@RestController
@RequestMapping("/bid")
public class BidController {
    @Autowired
    private BidService bidService;

    @PostMapping
    public Result bidPrice(BidDTO bidDTO){
        log.info("用户出价:{}", bidDTO);
        bidService.bidPrice(bidDTO);
        return Result.success();
    }

}
