package qkl.zn.AuctionSystem.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemPageQueryDTO {
    private int page;

    private int pageSize;

    private String title;

    private Long MaxPrice;

    private Long MinPrice;

    private Integer status;
}
