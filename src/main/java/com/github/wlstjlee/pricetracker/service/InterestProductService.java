package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.dto.*;
import com.github.wlstjlee.pricetracker.entity.InterestProduct;
import com.github.wlstjlee.pricetracker.entity.PriceHistory;
import com.github.wlstjlee.pricetracker.exception.InterestProductNotFoundException;
import com.github.wlstjlee.pricetracker.repository.InterestProductRepository;
import com.github.wlstjlee.pricetracker.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterestProductService {

    private final InterestProductRepository interestProductRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductParsingService productParsingService;   // 추가

    @Transactional
    public InterestProductResponse create(InterestProductCreateRequest request) {
        ProductParseResult parsed = productParsingService.parse(request.getUrl());

        InterestProduct entity = InterestProduct.builder()
                .name(parsed.getName())
                .url(parsed.getUrl())
                .imageUrl(parsed.getImageUrl())
                .mallName("쿠팡")
                .currentLowestPrice(parsed.getPrice())
                .build();

        InterestProduct saved = interestProductRepository.save(entity);
        return InterestProductResponse.from(saved);
    }

    public List<InterestProductResponse> getAll() {
        return interestProductRepository.findAll().stream()
                .map(InterestProductResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!interestProductRepository.existsById(id)) {
            throw new InterestProductNotFoundException(id);
        }
        priceHistoryRepository.deleteByInterestProductId(id);
        interestProductRepository.deleteById(id);
    }

    public List<PriceHistoryResponse> getHistories(Long interestProductId) {
        return priceHistoryRepository.findByInterestProductIdOrderByCreatedAtDesc(interestProductId).stream()
                .map(PriceHistoryResponse::from)
                .toList();
    }
}