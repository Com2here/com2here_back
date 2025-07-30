package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.Spec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRespDto {
    private String cpu;
    private String gpu;
    private String line;
    private double totalScores;
    private int totalPrice;
    private String title;
    private String link;
    private String image;
    private int price;
    private String mall;
}