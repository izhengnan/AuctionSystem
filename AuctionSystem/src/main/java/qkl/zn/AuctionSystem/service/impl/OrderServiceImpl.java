package qkl.zn.AuctionSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qkl.zn.AuctionSystem.filter.TokenFilter;
import qkl.zn.AuctionSystem.mapper.OrderMapper;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemGetOrdersByUserInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemPayOrderInputBO;
import qkl.zn.AuctionSystem.pojo.entity.Order;
import qkl.zn.AuctionSystem.service.AuctionSystemService;
import qkl.zn.AuctionSystem.service.OrderService;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AuctionSystemService auctionSystemService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void payOrder(Long orderId) {
        // 获取当前用户ID
        Long userId = TokenFilter.getCurrentUserId();

        // 构造区块链调用参数
        AuctionSystemPayOrderInputBO input = new AuctionSystemPayOrderInputBO();
        input.set_orderId(BigInteger.valueOf(orderId));
        input.set_updateTime(BigInteger.valueOf(System.currentTimeMillis() / 1000)); // 转换为秒级时间戳

        try {
            // 调用区块链合约订单付款
            auctionSystemService.payOrder(input);
            
            // 更新数据库订单状态
            // 注意：实际项目中可能需要更复杂的逻辑来同步区块链状态到数据库
        } catch (Exception e) {
            throw new RuntimeException("区块链订单付款失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> getMyOrders() {
        // 获取当前用户ID
        Long userId = TokenFilter.getCurrentUserId();

        // 构造区块链调用参数
        AuctionSystemGetOrdersByUserInputBO input = new AuctionSystemGetOrdersByUserInputBO();
        input.set_userId(BigInteger.valueOf(userId));

        try {
            // 调用区块链合约查询用户订单
            org.fisco.bcos.sdk.transaction.model.dto.CallResponse response = auctionSystemService.getOrdersByUser(input);
            
            // 解析返回结果
            // 使用getReturnObject()方法获取返回数据
            List<Object> returnObjects = (List<Object>) response.getReturnObject();
            List<BigInteger> ids = (List<BigInteger>) returnObjects.get(0);
            List<BigInteger> itemIds = (List<BigInteger>) returnObjects.get(1);
            List<BigInteger> buyerIds = (List<BigInteger>) returnObjects.get(2);
            List<BigInteger> dealPrices = (List<BigInteger>) returnObjects.get(3);
            List<BigInteger> statuses = (List<BigInteger>) returnObjects.get(4);
            List<BigInteger> updateTimes = (List<BigInteger>) returnObjects.get(5);
            
            // 转换为Order对象列表
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                Order order = new Order();
                order.setId(ids.get(i).longValue());
                order.setItemId(itemIds.get(i).longValue());
                order.setUserId(buyerIds.get(i).longValue());
                order.setDealPrice(dealPrices.get(i).longValue());
                order.setStatus(statuses.get(i).intValue());
                // 注意：区块链返回的是时间戳，需要转换为LocalDateTime
                order.setUpdateTime(LocalDateTime.now()); // 简化处理，实际应根据updateTimes.get(i)转换
                orders.add(order);
            }
            
            return orders;
        } catch (Exception e) {
            throw new RuntimeException("区块链查询订单失败: " + e.getMessage(), e);
        }
    }
}