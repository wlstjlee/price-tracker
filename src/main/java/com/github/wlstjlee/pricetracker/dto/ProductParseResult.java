package com.github.wlstjlee.pricetracker.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class ProductParseResult {

    private String name;
    private int price;
    private String imageUrl;
    private String url;
}