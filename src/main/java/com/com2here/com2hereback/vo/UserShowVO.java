package com.com2here.com2hereback.vo;

import java.time.LocalDateTime;

import com.com2here.com2hereback.dto.UserShowRespDto;
import lombok.Value;

@Value
public class UserShowVO {
    String uuid;
    String nickname;
    String email;
    String profileImageUrl;
    boolean isVerified;
    String role;
    LocalDateTime createdAt;
    LocalDateTime lastLoginAt;

    public static UserShowVO from(UserShowRespDto dto) {
        return new UserShowVO(
            dto.getUuid(),
            dto.getNickname(),
            dto.getEmail(),
            dto.getProfileImageUrl(),
            dto.isVerified(),
            dto.getRole(),
            dto.getCreatedAt(),
            dto.getLastLoginAt()
        );
    }
}
