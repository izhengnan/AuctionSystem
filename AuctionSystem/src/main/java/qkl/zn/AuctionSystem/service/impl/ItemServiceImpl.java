package qkl.zn.AuctionSystem.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qkl.zn.AuctionSystem.mapper.ItemMapper;
import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;
import qkl.zn.AuctionSystem.result.PageResult;
import qkl.zn.AuctionSystem.service.ItemService;

import java.time.LocalDateTime;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void addItem(ItemDTO itemDTO) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO,item);
        item.setCurrentMaxPrice(item.getInitialPrice());
        item.setStatus(0);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        itemMapper.addItem(item);
    }

    @Override
    public PageResult selectItemList(ItemPageQueryDTO itemPageQueryDTO) {
        PageHelper.startPage(itemPageQueryDTO.getPage(),itemPageQueryDTO.getPageSize());
        PageResult pageResult = new PageResult();
        Page<Item> page = itemMapper.selectItemList(itemPageQueryDTO);
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }
}