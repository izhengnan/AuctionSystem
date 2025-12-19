package qkl.zn.AuctionSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qkl.zn.AuctionSystem.filter.TokenFilter;
import qkl.zn.AuctionSystem.mapper.OrderMapper;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemGetOrdersByUserInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemPayOrderInputBO;
import qkl.zn.AuctionSystem.pojo.entity.Order;
import qkl.zn.AuctionSystem.service.AuctionSystemService;
import qkl.zn.AuctionSystem.service.ItemService;
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
    
    @Autowired
    private ItemService itemService;

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
        
        // 检查用户是否已登录
        if (userId == null) {
            throw new RuntimeException("用户未登录，请先登录");
        }

        // 构造区块链调用参数
        AuctionSystemGetOrdersByUserInputBO input = new AuctionSystemGetOrdersByUserInputBO();
        input.set_userId(BigInteger.valueOf(userId));

        try {
            // 调用区块链合约查询用户订单
            // 返回6个数组: [ids[], itemIds[], buyerIds[], dealPrices[], statuses[], updateTimes[]]
            org.fisco.bcos.sdk.transaction.model.dto.CallResponse response = auctionSystemService.getOrdersByUser(input);
            
            // 解析返回结果
            List<Order> orders = new ArrayList<>();
            Object returnObject = response.getReturnObject();
            
            if (returnObject instanceof List) {
                List<?> returnList = (List<?>) returnObject;
                
                // 区块链返回结构为: [ids[], itemIds[], buyerIds[], dealPrices[], statuses[], updateTimes[]]
                if (returnList.size() >= 6) {
                    try {
                        List<?> ids = (List<?>) returnList.get(0);
                        List<?> itemIds = (List<?>) returnList.get(1);
                        List<?> buyerIds = (List<?>) returnList.get(2);
                        List<?> dealPrices = (List<?>) returnList.get(3);
                        List<?> statuses = (List<?>) returnList.get(4);
                        List<?> updateTimes = (List<?>) returnList.get(5);
                        
                        int length = ids.size();
                        
                        // 转换为Order对象列表
                        for (int i = 0; i < length; i++) {
                            Order order = new Order();
                            order.setId(convertToLong(ids.get(i)));
                            order.setItemId(convertToLong(itemIds.get(i)));
                            order.setUserId(convertToLong(buyerIds.get(i)));
                            order.setDealPrice(convertToLong(dealPrices.get(i)));
                            order.setStatus(convertToInt(statuses.get(i)));
                            
                            // 转换时间戳（秒）为 LocalDateTime
                            long timestampSeconds = convertToLong(updateTimes.get(i));
                            order.setUpdateTime(java.time.Instant.ofEpochSecond(timestampSeconds)
                                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                            
                            // 查询拍品信息以获取itemTitle
                            try {
                                qkl.zn.AuctionSystem.pojo.entity.Item item = itemService.selectItemById(order.getItemId());
                                if (item != null) {
                                    order.setItemTitle(item.getTitle());
                                }
                            } catch (Exception e) {
                                // 忽略查询拍品信息的错误
                            }
                            
                            orders.add(order);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("转换业务数据失败: " + e.getMessage(), e);
                    }
                } else {
                    throw new RuntimeException("区块链返回数据结构不正确，期望至少6个数组，实际返回: " + returnList.size());
                }
            } else {
                throw new RuntimeException("区块链返回数据格式不正确: " + (returnObject != null ? returnObject.getClass().getName() : "null"));
            }
            
            return orders;
        } catch (Exception e) {
            throw new RuntimeException("区块链查询订单失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 灵活的类型转换方法
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
    
    /**
     * 灵活的int类型转换方法
     */
    private Integer convertToInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Long) {
            return ((Long) obj).intValue();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).intValue();
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}