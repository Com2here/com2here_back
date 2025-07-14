package com.com2here.com2hereback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private String title;
    private String link;
    private String image;
    private int price;
    private String mall;
}
