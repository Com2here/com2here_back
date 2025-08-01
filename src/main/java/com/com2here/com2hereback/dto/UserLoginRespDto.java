package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginRespDto {
    private String accessToken;
    private String refreshToken;
    private String nickname;
    private String email;
    private boolean isEmailVerified;
    private String profileImageUrl;
    private String role;
    private String provider;

    public static UserLoginRespDto entityToDto(User user, String accessToken, String refreshToken, String provider) {
        return UserLoginRespDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .nickname(user.getNickname())
            .email(user.getEmail())
            .isEmailVerified(user.isEmailVerified())
            .profileImageUrl(user.getProfileImageUrl())
            .role(user.getRole().name())
            .provider(provider)
            .build();
    }
}
