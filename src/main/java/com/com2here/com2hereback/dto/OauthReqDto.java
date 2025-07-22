package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OauthReqDto {
    @NotBlank(message = "code는 필수입니다.")
    private String code;
}
