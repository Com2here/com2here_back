package com.com2here.com2hereback.dto.oauthinfo;

import com.com2here.com2hereback.dto.oauthaccount.KakaoAccount;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoInfo {
    private KakaoAccount kakaoAccount;

    public static KakaoInfo fail() {
        return null;
    }
}
