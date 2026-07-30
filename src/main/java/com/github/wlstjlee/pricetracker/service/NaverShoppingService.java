package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.dto.NaverSearchResponse;
import com.github.wlstjlee.pricetracker.dto.ProductSearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NaverShoppingService { // 네이버 api 이용해서 product search 결과 받아오기

    private final RestTemplate restTemplate;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public List<ProductSearchDTO> search(String keyword, Integer display, String sort){
        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/shop.json")
                .queryParam("query",keyword)
                .queryParam("display", display)
                .queryParam("sort", sort)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id",clientId);
        headers.add("X-Naver-Client-Secret",clientSecret);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponse> response = restTemplate.exchange(uri,
                HttpMethod.GET, httpEntity, NaverSearchResponse.class);

        return response.getBody().getItems().stream().map(ProductSearchDTO::from).toList();
    }





}
