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
    private KakaoAccount kakaoAccount; // ✅ 사용자 정보 포함
    // ✅ 실패 응답을 반환하는 정적 메서드 추가

    public static KakaoInfo fail() {
        return new KakaoInfo(); // 실패 시 빈 객체 반환
    }
}