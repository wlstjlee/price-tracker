package com.github.wlstjlee.pricetracker.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("본인의 관심상품만 접근할 수 있습니다");
    }
}