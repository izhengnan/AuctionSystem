package qkl.zn.AuctionSystem.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;

@Mapper
public interface ItemMapper {
    void addItem(Item item);

    Page<Item> selectItemList(ItemPageQueryDTO itemPageQueryDTO);
    
    Item selectItemById(Long id);
}