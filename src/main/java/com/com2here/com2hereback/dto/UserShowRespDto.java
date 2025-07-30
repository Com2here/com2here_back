package com.com2here.com2hereback.dto;

import java.time.LocalDateTime;

import com.com2here.com2hereback.domain.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShowRespDto {
    private String uuid;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private boolean isVerified;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public static UserShowRespDto entityToDto(User user){
        return UserShowRespDto.builder()
            .uuid(user.getUuid())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .profileImageUrl(user.getProfileImageUrl())
            .isVerified(user.isEmailVerified())
            .role(user.getRole().name())
            .createdAt(user.getCreatedAt())
            .lastLoginAt(user.getLastLoginAt())
            .build();
    }
}
