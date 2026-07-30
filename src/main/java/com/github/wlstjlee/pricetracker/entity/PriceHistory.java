package com.github.wlstjlee.pricetracker.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PriceHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_product_id")
    private InterestProduct interestProduct;

    @Builder
    public PriceHistory(int price, InterestProduct interestProduct){
        this.price = price;
        this.interestProduct = interestProduct;
    }
}
