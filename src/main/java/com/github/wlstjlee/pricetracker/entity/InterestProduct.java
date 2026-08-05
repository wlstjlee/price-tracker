package com.github.wlstjlee.pricetracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class InterestProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;    // 상품명
    private String url;     // 상품 URL
    private String imageUrl;    // 이미지 링크
    private String mallName;    // 판매 쇼핑몰 이름
    private int currentLowestPrice; // 현재 최저가

    @Builder
    public InterestProduct(String name, String url, String imageUrl, String mallName, int currentLowestPrice){
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.mallName = mallName;
        this.currentLowestPrice = currentLowestPrice;
    }

    // 스케쥴러 최저가 갱신용
    public void updateLowestPrice(int newPrice){
        this.currentLowestPrice = newPrice;
    }
}
