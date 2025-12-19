package qkl.zn.AuctionSystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qkl.zn.AuctionSystem.pojo.dto.ItemDTO;
import qkl.zn.AuctionSystem.pojo.dto.ItemPageQueryDTO;
import qkl.zn.AuctionSystem.pojo.entity.Item;
import qkl.zn.AuctionSystem.result.PageResult;
import qkl.zn.AuctionSystem.result.Result;
import qkl.zn.AuctionSystem.service.ItemService;
import qkl.zn.AuctionSystem.utils.PermissionChecker;

import java.util.ArrayList;

@RestController
@RequestMapping("/item")
@Slf4j
@CrossOrigin
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PutMapping("/add")
    public Result addItem(@RequestBody ItemDTO itemDTO){
        log.info("添加拍品:{}", itemDTO);
        itemService.addItem(itemDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<PageResult> selectItemList(ItemPageQueryDTO itemPageQueryDTO){
        log.info("查询拍品列表:{}", itemPageQueryDTO);
        PageResult pageResult = itemService.selectItemList(itemPageQueryDTO);
        return Result.success(pageResult);
    }
    
    @GetMapping("/{itemId}")
    public Result<Item> selectItemById(@PathVariable Long itemId){
        log.info("查询拍品详情:id={}", itemId);
        Item item = itemService.selectItemById(itemId);
        return Result.success(item);
    }
    @DeleteMapping()
    public Result deleteItemByIds(@RequestParam("ids") ArrayList<Long> ids){
        log.info("删除拍品:{}", ids);
        itemService.deleteItemByIds(ids);
        return Result.success();
    }

    @PutMapping("update")
    public Result updateItem(@RequestBody ItemDTO itemDTO){
        log.info("更新拍品:{}", itemDTO);
        itemService.updateItem(itemDTO);
        return Result.success();
    }
}