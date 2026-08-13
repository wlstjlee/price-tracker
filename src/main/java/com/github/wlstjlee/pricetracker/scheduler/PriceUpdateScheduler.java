package com.github.wlstjlee.pricetracker.scheduler;

import com.github.wlstjlee.pricetracker.dto.ProductParseResult;
import com.github.wlstjlee.pricetracker.entity.InterestProduct;
import com.github.wlstjlee.pricetracker.entity.PriceHistory;
import com.github.wlstjlee.pricetracker.repository.InterestProductRepository;
import com.github.wlstjlee.pricetracker.repository.PriceHistoryRepository;
import com.github.wlstjlee.pricetracker.service.ProductParsingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class PriceUpdateScheduler {

    private final InterestProductRepository interestProductRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductParsingService productParsingService;

    @Scheduled(cron = "0 */5 * * * *")   // 개발 중엔 5분마다 (나중에 실제 운영시엔 조정)
    public void updateLowestPrices() {
        List<InterestProduct> products = interestProductRepository.findAll();

        for (InterestProduct product : products) {
            try {
                ProductParseResult parsed = productParsingService.parse(product.getUrl());

                product.updateLowestPrice(parsed.getPrice());

                PriceHistory history = PriceHistory.builder()
                        .price(parsed.getPrice())
                        .interestProduct(product)
                        .build();
                priceHistoryRepository.save(history);

            } catch (Exception e) {
                // 특정 상품 실패해도 나머지는 계속 진행
                log.error("가격 갱신 실패: {}", product.getUrl(), e);
            }
        }
    }
}