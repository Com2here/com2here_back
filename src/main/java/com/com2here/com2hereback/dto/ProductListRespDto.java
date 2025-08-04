package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.Product;
import com.com2here.com2hereback.domain.Spec;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductListRespDto {

    private long totalElements;
    private int totalPages;
    private int currentPage;
    private List<ProductInfo> products;

    @Getter
    @NoArgsConstructor
    public static class ProductInfo {
        private Long productId;
        private Long naverProductId;
        private String title;
        private String image;
        private String line;
        private String mall;
        private Long price;
        private Long totalPrice;
        private Double totalScores;
        private SpecDto spec;

        @Builder
        public ProductInfo(Long productId,
                           Long naverProductId,
                           String title,
                           String image,
                           String line,
                           String mall,
                           Long price,
                           Long totalPrice,
                           Double totalScores,
                           SpecDto spec) {
            this.productId = productId;
            this.naverProductId = naverProductId;
            this.title = title;
            this.image = image;
            this.line = line;
            this.mall = mall;
            this.price = price;
            this.totalPrice = totalPrice;
            this.totalScores = totalScores;
            this.spec = spec;
        }
    }

    @Getter
    @Builder
    public static class SpecDto {
        private String cpu;
        private String gpu;

        public static SpecDto fromEntity(Spec spec) {
            if (spec == null) return null;
            return SpecDto.builder()
                    .cpu(spec.getCpu())
                    .gpu(spec.getGpu())
                    .build();
        }
    }

    public static ProductListRespDto entityToDto(Page<Product> productPage, int page) {
        List<ProductInfo> productInfoList = productPage.getContent().stream()
            .map(product -> ProductInfo.builder()
                .productId(product.getProductId())
                .naverProductId(product.getNaverProductId())
                .title(product.getTitle())
                .image(product.getImage())
                .line(product.getLine())
                .mall(product.getMall())
                .price(product.getPrice())
                .totalPrice(product.getTotalPrice())
                .totalScores(product.getTotalScores())
                .spec(SpecDto.fromEntity(product.getSpec()))
                .build())
            .collect(Collectors.toList());

        return ProductListRespDto.builder()
            .totalElements(productPage.getTotalElements())
            .totalPages(productPage.getTotalPages())
            .currentPage(page)
            .products(productInfoList)
            .build();
    }
}
