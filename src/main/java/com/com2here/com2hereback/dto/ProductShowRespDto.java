package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.Product;
import com.com2here.com2hereback.domain.Spec;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductShowRespDto {

    private Long productId;
    private Long naverProductId;
    private String title;
    private String image;
    private String line;
    private String link;
    private String mall;
    private Long price;
    private Long totalPrice;
    private Double totalScores;
    private SpecDto spec;

    @Data
    @Builder
    public static class SpecDto {
        private String cpu;
        private String gpu;

        public static SpecDto fromEntity(Spec spec) {
            return SpecDto.builder()
                    .cpu(spec.getCpu())
                    .gpu(spec.getGpu())
                    .build();
        }
    }

    public static ProductShowRespDto entityToDto(Product product) {
        return ProductShowRespDto.builder()
            .productId(product.getProductId())
            .naverProductId(product.getNaverProductId())
            .title(product.getTitle())
            .image(product.getImage())
            .line(product.getLine())
            .link(product.getLink())
            .mall(product.getMall())
            .price(product.getPrice())
            .totalPrice(product.getTotalPrice())
            .totalScores(product.getTotalScores())
            .spec(product.getSpec() != null ? SpecDto.fromEntity(product.getSpec()) : null)
            .build();
    }
}