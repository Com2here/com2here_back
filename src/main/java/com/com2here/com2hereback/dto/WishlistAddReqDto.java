package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WishlistAddReqDto {

    @NotBlank(message = "CPU 정보는 필수입니다.")
    private String cpu;

    @NotBlank(message = "GPU 정보는 필수입니다.")
    private String gpu;

    @NotBlank(message = "상품 이미지는 필수입니다.")
    private String image;

    @NotBlank(message = "상품 라인은 필수입니다.")
    private String line;

    @NotBlank(message = "상품 상세 링크는 필수입니다.")
    private String link;

    @NotBlank(message = "쇼핑몰 이름은 필수입니다.")
    private String mall;

    @NotNull(message = "상품 가격은 필수입니다.")
    private Long price;

    @NotNull(message = "네이버 상품 ID는 필수입니다.")
    private Long naverProductId;

    @NotBlank(message = "상품명은 필수입니다.")
    private String title;

    @NotNull(message = "총 가격은 필수입니다.")
    private Long totalPrice;

    @NotNull(message = "상품 점수는 필수입니다.")
    private Double totalScores;

    @Builder
    public WishlistAddReqDto(String cpu,
                            String gpu,
                            String image,
                            String line,
                            String link,
                            String mall,
                            Long price,
                            Long naverProductId,
                            String title,
                            Long totalPrice,
                            Double totalScores) {
        this.cpu = cpu;
        this.gpu = gpu;
        this.image = image;
        this.line = line;
        this.link = link;
        this.mall = mall;
        this.price = price;
        this.naverProductId = naverProductId;
        this.title = title;
        this.totalPrice = totalPrice;
        this.totalScores = totalScores;
    }
}
