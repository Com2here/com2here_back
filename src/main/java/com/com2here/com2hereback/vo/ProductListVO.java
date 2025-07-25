package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.domain.Spec;
import com.com2here.com2hereback.dto.ProductListRespDto;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class ProductListVO {
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private List<ProductInfo> products;

    @Value
    @AllArgsConstructor
    public static class ProductInfo {
        Long productId;
        String image;
        SpecInfo specs;
        int price;
    }

    @Value
    public static class SpecInfo {
        String CPU;
        String memory;
        String graphicCard;
        String SSD;
        String mainBoard;
        String power;
        String case_;

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

    public static ProductListVO from(ProductListRespDto dto) {
        List<ProductInfo> productInfoList = dto.getProducts().stream()
            .map(product -> new ProductInfo(
                product.getProductId(),
                product.getImage(),
                new SpecInfo(product.getSpec()),
                product.getPrice()
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
