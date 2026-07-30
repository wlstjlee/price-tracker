package com.github.wlstjlee.pricetracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverSearchResponse {  // Naver Api 응답을 받아올 DTO

    private List<NaverItem> items;

    @Getter
    @NoArgsConstructor
    public static class NaverItem{
        private String title;
        private String link;
        private String image;
        private String lprice;
        private String mallName;
        private String productId;
    }


}
