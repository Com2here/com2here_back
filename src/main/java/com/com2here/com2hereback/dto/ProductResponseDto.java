package com.com2here.com2hereback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private String title;     // 상품 이름 (ex: "게이밍PC RTX 5060 + Ryzen5")
    private String link;      // 상품 링크 URL
    private String image;     // 썸네일 이미지 URL
    private int price;        // 최저가 (lprice)
    private String mall;      // 판매처 이름
}
