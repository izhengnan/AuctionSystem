package qkl.zn.AuctionSystem.service;

import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;
import qkl.zn.AuctionSystem.result.PageResult;

public interface ItemService {
    void addItem(ItemDTO itemDTO);

    PageResult selectItemList(ItemPageQueryDTO itemPageQueryDTO);
    
    Item selectItemById(Long id);
}