package com.github.wlstjlee.pricetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterestProductCreateRequest {

    @NotBlank(message = "상품 URL은 필수입니다")
    private String url;
}