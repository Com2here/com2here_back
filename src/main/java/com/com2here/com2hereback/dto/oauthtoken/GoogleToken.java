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
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType;

    public static GoogleToken fail() {
        return new GoogleToken(null, null);
    }

    private GoogleToken(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
