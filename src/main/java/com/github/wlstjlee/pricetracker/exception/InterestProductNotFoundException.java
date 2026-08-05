package com.github.wlstjlee.pricetracker.exception;

public class InterestProductNotFoundException extends RuntimeException {

    public InterestProductNotFoundException(Long id) {
        super("관심상품을 찾을 수 없습니다. id=" + id);
    }
}