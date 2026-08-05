package com.github.wlstjlee.pricetracker.dto;

// 관심상품 추가 후 응답 DTO

import com.github.wlstjlee.pricetracker.entity.InterestProduct;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InterestProductResponse {

    private Long id;
    private String name;
    private String url;
    private String imageUrl;
    private String mallName;
    private int currentLowestPrice;
    private LocalDateTime createdAt;

    public static InterestProductResponse from(InterestProduct interestProduct){
        return InterestProductResponse.builder()
                .id(interestProduct.getId())
                .name(interestProduct.getName())
                .url(interestProduct.getUrl())
                .imageUrl(interestProduct.getImageUrl())
                .mallName(interestProduct.getMallName())
                .currentLowestPrice(interestProduct.getCurrentLowestPrice())
                .createdAt(interestProduct.getCreatedAt())
                .build();
    }
}