package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.OauthResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
public class OauthResponseVo {
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;

    public static OauthResponseVo dtoToVo(OauthResponseDto dto) {
        return OauthResponseVo.builder()
            .email(dto.getEmail())
            .nickname(dto.getNickname())
            .accessToken(dto.getAccessToken())
            .refreshToken(dto.getRefreshToken())
            .build();
    }

    @Builder
    public OauthResponseVo(String email, String nickname, String accessToken, String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
