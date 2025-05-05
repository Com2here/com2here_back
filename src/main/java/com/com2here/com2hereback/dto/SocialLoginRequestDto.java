package com.com2here.com2hereback.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequestDto {
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider; // e.g., "kakao", "google", "naver"
    private String oauthId;  // 소셜 플랫폼의 유저 식별 ID
}
