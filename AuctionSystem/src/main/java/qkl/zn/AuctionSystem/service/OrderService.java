package qkl.zn.AuctionSystem.service;

import qkl.zn.AuctionSystem.pojo.entity.Order;

import java.util.List;

public interface OrderService {
    /**
     * 订单付款
     * @param orderId 订单ID
     */
    void payOrder(Long orderId);

    /**
     * 查询我的订单列表
     * @return 订单列表
     */
    List<Order> getMyOrders();
}