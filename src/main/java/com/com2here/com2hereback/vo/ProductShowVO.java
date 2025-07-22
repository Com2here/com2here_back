package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.domain.Spec;
import com.com2here.com2hereback.dto.ProductShowRespDto;
import lombok.Value;

@Value
public class ProductShowVO {
    Long productId;
    String image;
    SpecInfo specs;
    int price;

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

    public static ProductShowVO from(ProductShowRespDto dto) {
        return new ProductShowVO(
            dto.getProductId(),
            dto.getImage(),
            new SpecInfo(dto.getSpec()),
            dto.getPrice()
        );
    }
}