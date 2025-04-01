package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.OauthResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
public class OauthResponseVo {
    private String email;
    private String username;
    private String accessToken;
    private String refreshToken;

    public static OauthResponseVo dtoToVo(OauthResponseDto dto) {
        return OauthResponseVo.builder()
            .email(dto.getEmail())
            .username(dto.getUsername())
            .accessToken(dto.getAccessToken())
            .refreshToken(dto.getRefreshToken())
            .build();
    }

    @Builder
    public OauthResponseVo(String email, String username, String accessToken, String refreshToken) {
        this.email = email;
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
