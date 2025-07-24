package com.com2here.com2hereback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    // 알고리즘 결과
    private String cpu;
    private String gpu;
    private String line;
    private double totalScores;
    private int totalPrice;

    // 네이버 상품 정보
    private String title;
    private String link;
    private String image;
    private int price;
    private String mall;
}