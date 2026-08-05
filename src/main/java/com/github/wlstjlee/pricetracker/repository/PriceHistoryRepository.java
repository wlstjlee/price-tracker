package com.github.wlstjlee.pricetracker.repository;

import com.github.wlstjlee.pricetracker.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findByInterestProductIdOrderByCreatedAtDesc(Long id);

    void deleteByInterestProductId(Long interestProductId);
}
