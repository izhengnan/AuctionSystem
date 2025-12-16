package qkl.zn.AuctionSystem.service;

import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;
import qkl.zn.AuctionSystem.result.PageResult;

import java.util.ArrayList;

public interface ItemService {
    void addItem(ItemDTO itemDTO);

    PageResult selectItemList(ItemPageQueryDTO itemPageQueryDTO);
    
    Item selectItemById(Long id);

    void deleteItemByIds(ArrayList<Long> id);

    void updateItem(ItemDTO itemDTO);
}