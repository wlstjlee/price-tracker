package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.dto.*;
import com.github.wlstjlee.pricetracker.entity.InterestProduct;
import com.github.wlstjlee.pricetracker.entity.Member;
import com.github.wlstjlee.pricetracker.exception.InterestProductNotFoundException;
import com.github.wlstjlee.pricetracker.exception.UnauthorizedAccessException;
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
    private final ProductParsingService productParsingService;

    @Transactional
    public InterestProductResponse create(InterestProductCreateRequest request, Member member) {
        ProductParseResult parsed = productParsingService.parse(request.getUrl());

        InterestProduct entity = InterestProduct.builder()
                .name(parsed.getName())
                .url(parsed.getUrl())
                .imageUrl(parsed.getImageUrl())
                .mallName("무신사")
                .currentLowestPrice(parsed.getPrice())
                .member(member)
                .build();

        InterestProduct saved = interestProductRepository.save(entity);
        return InterestProductResponse.from(saved);
    }

    public List<InterestProductResponse> getAll(Member member) {
        return interestProductRepository.findByMemberId(member.getId()).stream()
                .map(InterestProductResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id, Member member) {
        InterestProduct product = interestProductRepository.findById(id)
                .orElseThrow(() -> new InterestProductNotFoundException(id));

        validateOwner(product, member);

        priceHistoryRepository.deleteByInterestProductId(id);
        interestProductRepository.deleteById(id);
    }

    public List<PriceHistoryResponse> getHistories(Long interestProductId, Member member) {
        InterestProduct product = interestProductRepository.findById(interestProductId)
                .orElseThrow(() -> new InterestProductNotFoundException(interestProductId));

        validateOwner(product, member);

        return priceHistoryRepository.findByInterestProductIdOrderByCreatedAtDesc(interestProductId).stream()
                .map(PriceHistoryResponse::from)
                .toList();
    }

    private void validateOwner(InterestProduct product, Member member) {
        if (!product.getMember().getId().equals(member.getId())) {
            throw new UnauthorizedAccessException();
        }
    }
}