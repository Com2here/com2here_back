package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.ProductShowRespDto;
import lombok.Value;

@Value
public class ProductShowVO {
    Long productId;
    String image;
    SpecInfo specs;
    Long price;

    @Value
    public static class SpecInfo {
        String cpu;
        String gpu;

        public static SpecInfo fromDto(ProductShowRespDto.SpecDto specDto) {
            if (specDto == null) return null;
            return new SpecInfo(specDto.getCpu(), specDto.getGpu());
        }
    }

    public static ProductShowVO from(ProductShowRespDto dto) {
        return new ProductShowVO(
            dto.getProductId(),
            dto.getImage(),
            SpecInfo.fromDto(dto.getSpec()),
            dto.getPrice()
        );
    }
}
