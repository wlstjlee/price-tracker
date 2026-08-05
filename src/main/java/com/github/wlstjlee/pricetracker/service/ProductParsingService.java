package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.dto.ProductParseResult;
import com.github.wlstjlee.pricetracker.exception.ProductParseFailedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class ProductParsingService {

    public ProductParseResult parse(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept-Language", "ko-KR,ko;q=0.9")
                    .header("Referer", "https://www.coupang.com/")
                    .timeout(10000)
                    .ignoreHttpErrors(false)
                    .get();

            // 상품명
            String name = doc.selectFirst("h1.product-title span.twc-font-bold").text();

            // 가격 - class에 대괄호가 있어서, 부모 태그 구조로 접근
            Element priceContainer = doc.selectFirst("div.price-layout-container");
            String priceText = priceContainer.selectFirst("span").text();  // "35,910"
            int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));

            // 이미지
            String imageUrl = doc.selectFirst("div.product-image img").attr("src");
            if (imageUrl.startsWith("//")) {
                imageUrl = "https:" + imageUrl;   // 프로토콜 없는 URL 보정
            }

            return ProductParseResult.builder()
                    .name(name)
                    .price(price)
                    .imageUrl(imageUrl)
                    .url(url)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductParseFailedException(url);
        }
    }
}