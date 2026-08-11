package com.github.wlstjlee.pricetracker.exception;

public class ProductParseFailedException extends RuntimeException {
    public ProductParseFailedException(String url) {
        super("상품 정보를 가져올 수 없습니다: " + url);
    }

    public ProductParseFailedException(String url, Throwable cause) {
        super("상품 정보를 가져올 수 없습니다: " + url, cause);   // cause로 원인 함께 저장
    }
}