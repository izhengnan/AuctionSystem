package qkl.zn.AuctionSystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qkl.zn.AuctionSystem.pojo.entity.Order;
import qkl.zn.AuctionSystem.result.Result;
import qkl.zn.AuctionSystem.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单付款
     * @param orderId 订单ID
     * @return 结果
     */
    @PostMapping("/pay/{orderId}")
    public Result payOrder(@PathVariable Long orderId) {
        log.info("订单付款: orderId={}", orderId);
        orderService.payOrder(orderId);
        return Result.success();
    }

    /**
     * 我的订单列表
     * @return 订单列表
     */
    @GetMapping("/my")
    public Result<List<Order>> getMyOrders() {
        log.info("查询我的订单列表");
        List<Order> orders = orderService.getMyOrders();
        return Result.success(orders);
    }
}