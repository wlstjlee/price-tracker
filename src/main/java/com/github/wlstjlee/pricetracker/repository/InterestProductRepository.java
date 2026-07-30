package com.github.wlstjlee.pricetracker.repository;

import com.github.wlstjlee.pricetracker.entity.InterestProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestProductRepository extends JpaRepository<InterestProduct, Long> {
}
