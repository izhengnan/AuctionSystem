package qkl.zn.AuctionSystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import qkl.zn.AuctionSystem.pojo.entity.Item;

@Mapper
public interface ItemMapper {
    void addItem(Item item);
}
