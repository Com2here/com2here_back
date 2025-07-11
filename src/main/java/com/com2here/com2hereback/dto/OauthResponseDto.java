package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.Role;
import com.com2here.com2hereback.dto.oauth.GoogleInfo;
import com.com2here.com2hereback.dto.oauth.GoogleToken;
import com.com2here.com2hereback.dto.oauth.KakaoInfo;
import com.com2here.com2hereback.dto.oauth.KakaoToken;
import com.com2here.com2hereback.dto.oauth.NaverInfo;
import com.com2here.com2hereback.dto.oauth.NaverToken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OauthResponseDto {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String nickname;
    private String role;
    private String provider;
    private String oauthId;


    public static OauthResponseDto entityToDto(KakaoInfo kakaoInfo, KakaoToken kakaoToken) {
        return OauthResponseDto.builder()
            .email(kakaoInfo.getEmail())
            .nickname(kakaoInfo.getNickname())
            .accessToken(kakaoToken.getAccessToken())
            .refreshToken(kakaoToken.getRefreshToken())
            .role(Role.SOCIAL.name())
            .provider("kakao")
            .oauthId(String.valueOf(kakaoInfo.getId()))
            .build();
    }

    public static OauthResponseDto entityToDto(NaverInfo naverInfo, NaverToken naverToken) {
        return OauthResponseDto.builder()
            .email(naverInfo.getEmail())
            .nickname(naverInfo.getName())
            .accessToken(naverToken.getAccessToken())
            .refreshToken(naverToken.getRefreshToken())
            .role(Role.SOCIAL.name())
            .provider("naver")
            .oauthId(naverInfo.getId())
            .build();
    }

    public static OauthResponseDto entityToDto(GoogleInfo googleInfo, GoogleToken googleToken) {
        return OauthResponseDto.builder()
            .email(googleInfo.getEmail())
            .nickname(googleInfo.getName())
            .accessToken(googleToken.getAccessToken())
            .refreshToken(googleToken.getRefreshToken())
            .role(Role.SOCIAL.name())
            .provider("google")
            .oauthId(googleInfo.getSub())
            .build();
    }
}
