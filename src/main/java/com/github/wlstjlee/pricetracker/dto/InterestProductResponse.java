package com.github.wlstjlee.pricetracker.dto;

// 관심상품 추가 후 응답 DTO

import com.github.wlstjlee.pricetracker.entity.InterestProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestProductResponse {

    private Long id;
    private String name;    // 상품명
    private String url;     // 상품 URL
    private String imageUrl;    // 이미지 링크
    private String mallName;    // 판매 쇼핑몰 이름
    private String naverProductId;  // 네이버 상품 고유Id ( 최저가 갱신시 필요 )
    private int currentLowestPrice; // 현재 최저가

    public static InterestProductResponse from(InterestProduct interestProduct){
        return InterestProductResponse.builder()
                .id(interestProduct.getId())
                .name(interestProduct.getName())
                .url(interestProduct.getUrl())
                .imageUrl(interestProduct.getImageUrl())
                .mallName(interestProduct.getMallName())
                .naverProductId(interestProduct.getNaverProductId())
                .currentLowestPrice(interestProduct.getCurrentLowestPrice())
                .build();
    }
}
