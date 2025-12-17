package qkl.zn.AuctionSystem.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private Long id;
    private Long itemId;
    private Long userId;
    private Long dealPrice;
    private Integer status;
    private LocalDateTime updateTime;
}