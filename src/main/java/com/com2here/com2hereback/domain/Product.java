package com.com2here.com2hereback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spec_id", nullable = false)
    private Spec spec;

    @Column(nullable = false)
    private int price;

    @Builder
    public Product(Long product_id, Spec spec, String image, int price) {
        this.product_id = product_id;
        this.image = image;
        this.spec = spec;
        this.price = price;
    }
}
