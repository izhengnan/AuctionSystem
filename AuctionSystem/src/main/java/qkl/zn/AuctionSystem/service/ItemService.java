package qkl.zn.AuctionSystem.service;

import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;
import qkl.zn.AuctionSystem.result.PageResult;

import java.util.ArrayList;
import java.util.List;

public interface ItemService {
    void addItem(ItemDTO itemDTO);

    PageResult selectItemList(ItemPageQueryDTO itemPageQueryDTO);
    
    Item selectItemById(Long id);

    void deleteItemByIds(ArrayList<Long> ids);

    void updateItem(ItemDTO itemDTO);
    
    List<Item> getAllItems();
    
    void updateItemStatus(Long id, Integer status);
        
    void updateCurrentMaxPrice(Long itemId, Long currentMaxPrice, Long currentMaxUserId);

    void updateItemlistingStatusById(Long id, Integer listingStatus);
}