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
    private Long id; // ID由数据库自增生成
    private Long itemId;
    private Long userId;
    private Long dealPrice;
    private Integer status; // 状态由合约生成
    private LocalDateTime updateTime;
}