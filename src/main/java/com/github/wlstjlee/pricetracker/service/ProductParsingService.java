package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.dto.ProductParseResult;
import com.github.wlstjlee.pricetracker.exception.ProductParseFailedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ProductParsingService {

    public ProductParseResult parse(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();

            String rawName = doc.selectFirst("meta[property=og:title]").attr("content");
            String name = rawName.split(" - 사이즈")[0].trim();

            String priceText = doc.selectFirst("meta[property=product:price:amount]").attr("content");
            int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));

            String imageUrl = doc.selectFirst("meta[property=og:image]").attr("content");

            return ProductParseResult.builder()
                    .name(name)
                    .price(price)
                    .imageUrl(imageUrl)
                    .url(url)
                    .build();

        } catch (Exception e) {
            throw new ProductParseFailedException(url, e);
        }
    }
}