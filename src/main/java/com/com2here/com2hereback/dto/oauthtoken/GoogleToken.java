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
public class GoogleToken {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;

    public static GoogleToken fail() {
        return new GoogleToken(null, null, null, null);
    }

    private GoogleToken(final String accessToken, final String refreshToken, final String tokenType,
            final Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
