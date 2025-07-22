package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.OauthRespDto;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
public class OauthVO {
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
    private String role;
    private String provider;
    private String oauthId;

    @Builder
    public OauthVO(String email, String nickname, String accessToken, String refreshToken, String role, String provider, String oauthId) {
        this.email = email;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.provider = provider;
        this.oauthId = oauthId;
    }

    public static OauthVO dtoToVo(OauthRespDto dto) {
        return OauthVO.builder()
            .email(dto.getEmail())
            .nickname(dto.getNickname())
            .accessToken(dto.getAccessToken())
            .refreshToken(dto.getRefreshToken())
            .role(dto.getRole())
            .provider(dto.getProvider())
            .oauthId(dto.getOauthId())
            .build();
    }


}
