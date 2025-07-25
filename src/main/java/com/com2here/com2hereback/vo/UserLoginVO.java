package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.UserLoginRespDto;
import lombok.Value;

@Value
public class UserLoginVO {
    String accessToken;
    String refreshToken;
    String nickname;
    String email;
    boolean isEmailVerified;
    String profileImageUrl;
    String role;
    String provider;

    public static UserLoginVO from(UserLoginRespDto dto) {
        return new UserLoginVO(
            dto.getAccessToken(),
            dto.getRefreshToken(),
            dto.getNickname(),
            dto.getEmail(),
            dto.isEmailVerified(),
            dto.getProfileImageUrl(),
            dto.getRole(),
            dto.getProvider()
        );
    }
}
