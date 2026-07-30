package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.repository.InterestProductRepository;
import com.github.wlstjlee.pricetracker.repository.PriceHistoryRepository;
import com.github.wlstjlee.pricetracker.dto.InterestProductCreateRequest;
import com.github.wlstjlee.pricetracker.dto.InterestProductResponse;
import com.github.wlstjlee.pricetracker.dto.PriceHistoryResponse;
import com.github.wlstjlee.pricetracker.entity.InterestProduct;
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

    @Transactional
    public InterestProductResponse create(InterestProductCreateRequest interestProductCreateRequest){
        InterestProduct interestProduct = interestProductCreateRequest.toEntity();
        InterestProduct saved = interestProductRepository.save(interestProduct);
        return InterestProductResponse.from(saved);
    }

    public List<InterestProductResponse> getAll(){
        return interestProductRepository.findAll().stream().map(InterestProductResponse::from).toList();
    }

    @Transactional
    public void delete(Long id){
        if(!interestProductRepository.existsById(id)){
            //throw new InterestProductNotFoundException;
        }
        interestProductRepository.deleteById(id);
    }

    public List<PriceHistoryResponse> getHistories(Long id){
        return priceHistoryRepository.findByInterestProductIdOrderByCreatedAtDesc(id).stream()
                .map(PriceHistoryResponse::from).toList();
    }







}
