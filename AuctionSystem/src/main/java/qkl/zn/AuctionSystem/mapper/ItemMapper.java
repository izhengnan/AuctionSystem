package qkl.zn.AuctionSystem.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ItemMapper {
    void addItem(Item item);

    Page<Item> selectItemList(ItemPageQueryDTO itemPageQueryDTO);
    
    Item selectItemById(Long id);

    void deleteItemByIds(ArrayList<Long> ids);

    void updateItem(ItemDTO itemDTO);
    
    List<Item> selectAllItems();
    
    void updateItemStatus(Long id, Integer status);
}