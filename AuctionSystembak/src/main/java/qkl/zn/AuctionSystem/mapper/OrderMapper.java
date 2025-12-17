package qkl.zn.AuctionSystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import qkl.zn.AuctionSystem.pojo.entity.Order;

@Mapper
public interface OrderMapper {
    
    /**
     * 插入订单记录
     * @param order 订单信息
     */
    @Insert("INSERT INTO t_auction_order (id, item_id, user_id, deal_price, status, update_time) VALUES (#{id}, #{itemId}, #{userId}, #{dealPrice}, #{status}, #{updateTime})")
    void insertOrder(Order order);
}