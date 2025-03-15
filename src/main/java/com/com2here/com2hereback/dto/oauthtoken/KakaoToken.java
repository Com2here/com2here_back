package com.com2here.com2hereback.dto.oauthtoken;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoToken {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;

    // 실패 시 KakaoToken 객체를 반환
    public static KakaoToken fail() {
        return new KakaoToken(null, null, null, null);
    }

    // 생성자 (accessToken 포함)
    private KakaoToken(final String accessToken, final String refreshToken, final String tokenType,
            final Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
