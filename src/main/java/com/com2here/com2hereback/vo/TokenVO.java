package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.TokenRespDto;
import lombok.Value;

@Value
public class TokenVO {
    String accessToken;
    String refreshToken;

    public static TokenVO from(TokenRespDto dto) {
        return new TokenVO(
            dto.getAccessToken(),
            dto.getRefreshToken()
        );
    }
}
