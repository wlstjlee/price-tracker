package com.github.wlstjlee.pricetracker.dto;

import com.github.wlstjlee.pricetracker.entity.PriceHistory;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

// 가격이력 조회 DTO
@Getter
@Builder
public class PriceHistoryResponse {

    private Long id;
    private int price;
    private LocalDateTime recordedAt;

    public static PriceHistoryResponse from(PriceHistory priceHistory){
        return PriceHistoryResponse.builder()
                .id(priceHistory.getId())
                .price(priceHistory.getPrice())
                .recordedAt(priceHistory.getCreatedAt())
                .build();
    }
}
