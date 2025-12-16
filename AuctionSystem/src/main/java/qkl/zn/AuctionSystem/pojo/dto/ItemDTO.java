package qkl.zn.AuctionSystem.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemDTO {
    private Long id;
    private String title;
    private String image;
    private Long initialPrice;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}