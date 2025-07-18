package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.domain.Spec;
import com.com2here.com2hereback.dto.ProductShowResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
public class ProductShowResponseVo {
    private Long productId;
    private String image;
    private SpecInfo specs;
    private int price;

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
    public ProductShowResponseVo(Long productId, String image, SpecInfo specs, int price) {
        this.productId = productId;
        this.image = image;
        this.specs = specs;
        this.price = price;
    }

    public static ProductShowResponseVo dtoToVo(ProductShowResponseDto productShowRespDto) {
        return ProductShowResponseVo.builder()
            .productId(productShowRespDto.getProductId())
            .image(productShowRespDto.getImage())
            .specs(new SpecInfo(productShowRespDto.getSpec()))
            .price(productShowRespDto.getPrice())
            .build();
    }
}
