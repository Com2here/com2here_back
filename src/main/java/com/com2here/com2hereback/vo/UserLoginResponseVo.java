package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.UserLoginResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponseVo {
    private String accessToken;
    private String refreshToken;
    private String nickname;
    private String email;
    private boolean isEmailVerified;
    private String profileImageUrl;
    private String role;
    private String provider;

    @Builder
    public UserLoginResponseVo(String accessToken, String refreshToken, String nickname, String email, boolean isEmailVerified, String profileImageUrl, String role, String provider) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.email = email;
        this.isEmailVerified = isEmailVerified;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.provider = provider;
    }

    public static UserLoginResponseVo dtoToVo(UserLoginResponseDto userLoginResponseDto) {
        return UserLoginResponseVo.builder()
            .accessToken(userLoginResponseDto.getAccessToken())
            .refreshToken(userLoginResponseDto.getRefreshToken())
            .nickname(userLoginResponseDto.getNickname())
            .email(userLoginResponseDto.getEmail())
            .isEmailVerified(userLoginResponseDto.isEmailVerified())
            .profileImageUrl(userLoginResponseDto.getProfileImageUrl())
            .role(userLoginResponseDto.getRole())
            .provider(userLoginResponseDto.getProvider())
            .build();
    }
}
