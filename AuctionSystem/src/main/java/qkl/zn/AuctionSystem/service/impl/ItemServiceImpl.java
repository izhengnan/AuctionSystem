package qkl.zn.AuctionSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qkl.zn.AuctionSystem.mapper.ItemMapper;
import qkl.zn.AuctionSystem.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;
}
