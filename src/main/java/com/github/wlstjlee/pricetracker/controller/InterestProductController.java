package com.github.wlstjlee.pricetracker.controller;

import com.github.wlstjlee.pricetracker.dto.InterestProductCreateRequest;
import com.github.wlstjlee.pricetracker.dto.InterestProductResponse;
import com.github.wlstjlee.pricetracker.dto.PriceHistoryResponse;
import com.github.wlstjlee.pricetracker.entity.Member;
import com.github.wlstjlee.pricetracker.service.InterestProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interests")
public class InterestProductController {

    private final InterestProductService interestProductService;

    @PostMapping
    public ResponseEntity<InterestProductResponse> create(
            @RequestBody @Valid InterestProductCreateRequest request,
            @AuthenticationPrincipal Member member
    ) {
        InterestProductResponse response = interestProductService.create(request, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<InterestProductResponse> getAll(@AuthenticationPrincipal Member member) {
        return interestProductService.getAll(member);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Member member
    ) {
        interestProductService.delete(id, member);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/histories")
    public List<PriceHistoryResponse> getHistories(
            @PathVariable Long id,
            @AuthenticationPrincipal Member member
    ) {
        return interestProductService.getHistories(id, member);
    }
}
