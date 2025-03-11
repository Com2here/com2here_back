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
    private Long expiresIn;

    public static GoogleToken fail() {
        return new GoogleToken(null, null, null);
    }

    private GoogleToken(final String accessToken, final String tokenType, final Long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
