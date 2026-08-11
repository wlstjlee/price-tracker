package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.dto.ProductParseResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ProductParsingServiceTest {

    @Test
    void testMusinsaAccess() throws IOException {
        Document doc = Jsoup.connect("https://www.musinsa.com/products/5206468")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .timeout(10000)
                .get();

        System.out.println(doc.title());
    }

    @Test
    void parseTest() {
        ProductParsingService service = new ProductParsingService();
        ProductParseResult result = service.parse("https://www.musinsa.com/products/5206468");

        System.out.println("이름: " + result.getName());
        System.out.println("가격: " + result.getPrice());
        System.out.println("이미지: " + result.getImageUrl());
    }

    @Test
    void parseTest_다양한상품() {
        ProductParsingService service = new ProductParsingService();

        ProductParseResult result1 = service.parse("https://www.musinsa.com/products/4844733");
        ProductParseResult result2 = service.parse("https://www.musinsa.com/products/6914558");

        System.out.println(result1.getName());
        System.out.println(result1.getPrice());
        System.out.println(result1.getUrl());
        System.out.println(result1.getImageUrl());
        System.out.println(result2.getName());
        System.out.println(result2.getPrice());
        System.out.println(result2.getUrl());
        System.out.println(result2.getImageUrl());
    }
}