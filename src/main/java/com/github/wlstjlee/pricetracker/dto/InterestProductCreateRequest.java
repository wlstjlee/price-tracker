package com.github.wlstjlee.pricetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class InterestProductCreateRequest {

    // 상품 정보(이름, 가격, 이미지 등)는 URL을 스크래핑해서 자동으로 채워지므로,
    // 클라이언트는 URL만 전달하면 됨 (ProductParsingService 참고)

    @NotBlank(message = "상품 URL은 필수입니다")
    private String url;
}