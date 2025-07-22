package com.com2here.com2hereback.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRespDto {

    private String accessToken;
    private String refreshToken;

    public static TokenRespDto entityToDto(String accessToken, String refreshToken) {
        return TokenRespDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
