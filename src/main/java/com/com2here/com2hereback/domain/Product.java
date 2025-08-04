package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id") 
    private Long productId;

    @Column(name = "naver_product_id", nullable = false, unique = true)
    private Long naverProductId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "line", nullable = false)
    private String line;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "mall", nullable = false)
    private String mall;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "spec_id", nullable = false)
    private Spec spec;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Column(name = "total_scores", nullable = false)
    private Double totalScores;

    @Builder
    public Product(Long naverProductId,
                   String title,
                   String image,
                   String line,
                   String link,
                   String mall,
                   Spec spec,
                   Long price,
                   Long totalPrice,
                   Double totalScores) {
        this.naverProductId = naverProductId;
        this.title = title;
        this.image = image;
        this.line = line;
        this.link = link;
        this.mall = mall;
        this.spec = spec;
        this.price = price;
        this.totalPrice = totalPrice;
        this.totalScores = totalScores;
    }
}

