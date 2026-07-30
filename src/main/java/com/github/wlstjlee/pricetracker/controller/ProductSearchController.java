package com.github.wlstjlee.pricetracker.controller;

import com.github.wlstjlee.pricetracker.dto.ProductSearchDTO;
import com.github.wlstjlee.pricetracker.service.NaverShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductSearchController {

    private final NaverShoppingService naverShoppingService;

    @GetMapping("/api/search")
    public List<ProductSearchDTO> search(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "10") Integer display,
            @RequestParam(required = false, defaultValue = "sim") String sort){
        return naverShoppingService.search(keyword, display, sort);
    }

}
