package com.github.wlstjlee.pricetracker.controller;

import com.github.wlstjlee.pricetracker.dto.InterestProductCreateRequest;
import com.github.wlstjlee.pricetracker.dto.InterestProductResponse;
import com.github.wlstjlee.pricetracker.dto.PriceHistoryResponse;
import com.github.wlstjlee.pricetracker.service.InterestProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interests")
public class InterestProductController {

    private final InterestProductService interestProductService;

    @PostMapping
    public ResponseEntity<InterestProductResponse> create(@RequestBody @Valid InterestProductCreateRequest request){
        InterestProductResponse response = interestProductService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<InterestProductResponse> getAll(){
        return interestProductService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        interestProductService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/histories")
    public List<PriceHistoryResponse> getHistories(@PathVariable Long id){
        return interestProductService.getHistories(id);
    }
}
