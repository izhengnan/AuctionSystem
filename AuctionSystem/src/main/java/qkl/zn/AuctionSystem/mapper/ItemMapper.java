package qkl.zn.AuctionSystem.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemMapper {
    void addItem(Item item);

    Page<Item> selectItemList(ItemPageQueryDTO itemPageQueryDTO);
    
    Item selectItemById(Long id);

    void deleteItemByIds(@Param("ids") ArrayList<Long> ids);

    void updateItem(Item item);
    
    List<Item> selectAllItems();
    
    void updateItemStatus(Long id, Integer status);
        
        void updateCurrentMaxPrice(Long itemId, Long currentMaxPrice, Long currentMaxUserId);
}