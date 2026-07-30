package com.github.wlstjlee.pricetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 네이버 Api 검색결과 응답 DTO ( NaverSearchResponse ) 으로부터 클라이언트에 보여줄 DTO
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDTO {

    private String name;    // 상품명
    private String url; // 상품 URL
    private String imageUrl;    // 이미지 링크
    private String mallName;    // 판매 쇼핑몰 이름
    private String naverProductId;   // 네이버 상품 고유Id
    private int currentLowestPrice;    // 현재 최저가

    public static ProductSearchDTO from(NaverSearchResponse.NaverItem item){
        return ProductSearchDTO.builder()
                .name(item.getTitle())
                .url(item.getLink())
                .imageUrl(item.getImage())
                .mallName(item.getMallName())
                .naverProductId(item.getProductId())
                .currentLowestPrice(Integer.parseInt(item.getLprice()))
                .build();
    }
}


