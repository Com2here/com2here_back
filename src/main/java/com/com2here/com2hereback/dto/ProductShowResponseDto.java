package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.Product;
import com.com2here.com2hereback.domain.Spec;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductShowResponseDto {

    private Long productId;
    private String image;
    private Spec spec;
    private int price;

    public static ProductShowResponseDto entityToDto(Product product) {
        return ProductShowResponseDto.builder()
            .productId(product.getProductId())
            .image(product.getImage())
            .spec(product.getSpec())
            .price(product.getPrice())
            .build();
    }
}
