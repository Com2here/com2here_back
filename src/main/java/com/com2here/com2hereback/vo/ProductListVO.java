package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.ProductListRespDto;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class ProductListVO {
    long totalElements;
    int totalPages;
    int currentPage;
    List<ProductInfo> products;

    @Value
    public static class ProductInfo {
        Long productId;
        Long naverProductId;
        String title;
        String image;
        String line;
        String mall;
        Long price;
        Long totalPrice;
        Double totalScores;
        SpecInfo spec;
    }

    @Value
    public static class SpecInfo {
        String cpu;
        String gpu;

        public static SpecInfo fromDto(ProductListRespDto.SpecDto specDto) {
            if (specDto == null) return null;
            return new SpecInfo(specDto.getCpu(), specDto.getGpu());
        }
    }

    public static ProductListVO from(ProductListRespDto dto) {
        List<ProductInfo> productInfoList = dto.getProducts().stream()
            .map(product -> new ProductInfo(
                product.getProductId(),
                product.getNaverProductId(),
                product.getTitle(),
                product.getImage(),
                product.getLine(),
                product.getMall(),
                product.getPrice(),
                product.getTotalPrice(),
                product.getTotalScores(),
                SpecInfo.fromDto(product.getSpec())
            ))
            .collect(Collectors.toList());

        return new ProductListVO(
            dto.getTotalElements(),
            dto.getTotalPages(),
            dto.getCurrentPage(),
            productInfoList
        );
    }
}
