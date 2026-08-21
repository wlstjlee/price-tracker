package com.github.wlstjlee.pricetracker.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String token;
    private String email;
    private String name;
}
