package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.domain.Spec;
import com.com2here.com2hereback.dto.ProductListResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductListResponseVo {
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private List<ProductInfo> products;

    @Getter
    @NoArgsConstructor
    public static class ProductInfo {
        private Long productId;
        private String image;
        private SpecInfo specs;
        private int price;

        @Builder
        public ProductInfo(Long productId, String image, SpecInfo specs, int price) {
            this.productId = productId;
            this.image = image;
            this.specs = specs;
            this.price = price;
        }
    }

    @Data
    public static class SpecInfo {
        private String CPU;
        private String memory;
        private String graphicCard;
        private String SSD;
        private String mainBoard;
        private String power;
        private String case_;

        public SpecInfo(Spec spec) {
            this.CPU = spec.getCpu();
            this.memory = spec.getMemory();
            this.graphicCard = spec.getGraphicCard();
            this.SSD = spec.getSsd();
            this.mainBoard = spec.getMainBoard();
            this.power = spec.getPower();
            this.case_ = spec.getCaseName();
        }
    }

    @Builder
    public ProductListResponseVo(int totalElements, int totalPages, int currentPage, List<ProductInfo> products) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.products = products;
    }

    public static ProductListResponseVo dtoToVo(ProductListResponseDto dto) {
        List<ProductInfo> productInfoList = dto.getProducts().stream()
            .map(product -> ProductInfo.builder()
                .productId(product.getProductId())
                .image(product.getImage())
                .specs(new SpecInfo(product.getSpec()))
                .price(product.getPrice())
                .build())
            .collect(Collectors.toList());

        return ProductListResponseVo.builder()
            .totalElements(dto.getTotalElements())
            .totalPages(dto.getTotalPages())
            .currentPage(dto.getCurrentPage())
            .products(productInfoList)
            .build();
    }
}
