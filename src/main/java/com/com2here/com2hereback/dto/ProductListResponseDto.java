package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.Product;
import com.com2here.com2hereback.domain.Spec;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
public class ProductListResponseDto {

    private int totalElements;
    private int totalPages;
    private int currentPage;
    private List<ProductInfo> products;

    @Getter
    @NoArgsConstructor
    public static class ProductInfo {
        private Long product_id;
        private String image;
        private Spec spec;
        private int price;

        @Builder
        public ProductInfo(Long product_id, String image, Spec spec, int price) {
            this.product_id = product_id;
            this.image = image;
            this.spec = spec;
            this.price = price;
        }
    }

    public static ProductListResponseDto entityToDto(Page<Product> productPage, int page) {
        List<Product> products = productPage.getContent();
        int totalElements = (int) productPage.getTotalElements();

        List<ProductInfo> productInfoList = products.stream()
            .map(product -> ProductInfo.builder()
                .product_id(product.getProduct_id())
                .image(product.getImage())
                .spec(product.getSpec())
                .price(product.getPrice())
                .build())
            .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) totalElements / products.size());

        return ProductListResponseDto.builder()
            .totalElements(totalElements)
            .totalPages(totalPages)
            .currentPage(page)
            .products(productInfoList)
            .build();
    }
}
