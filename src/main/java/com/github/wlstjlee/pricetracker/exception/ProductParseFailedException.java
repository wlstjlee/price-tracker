package com.github.wlstjlee.pricetracker.exception;

public class ProductParseFailedException extends RuntimeException {
    public ProductParseFailedException(String url) {
        super("상품 정보를 가져올 수 없습니다: " + url);
    }
}