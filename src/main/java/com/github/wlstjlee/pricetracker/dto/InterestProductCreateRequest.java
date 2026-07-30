package com.github.wlstjlee.pricetracker.dto;

// 관심상품 추가용 DTO

import com.github.wlstjlee.pricetracker.entity.InterestProduct;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestProductCreateRequest {

    private String name;    // 상품명
    private String url; // 상품 URL
    private String imageUrl;    // 이미지 링크
    private String mallName;    // 판매 쇼핑몰 이름

    @NotBlank(message = "네이버 상품 ID 는 필수입니다. ")
    private String naverProductId;   // 네이버 상품 고유Id
    private int currentLowestPrice;    // 현재 최저가

    public InterestProduct toEntity(){
        return InterestProduct.builder()
                .name(this.getName())
                .url(this.getUrl())
                .imageUrl(this.getImageUrl())
                .mallName(this.getMallName())
                .naverProductId(this.naverProductId)
                .currentLowestPrice(this.currentLowestPrice)
                .build();
    }
}
