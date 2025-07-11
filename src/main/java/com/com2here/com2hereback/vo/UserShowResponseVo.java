package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.ShowUserResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShowResponseVo {
    private String nickname;
    private String email;
    private String profileImageUrl ;
    private boolean isVerified;
    private String role;

    @Builder
    public UserShowResponseVo(String nickname, String email, String profileImageUrl, boolean isVerified, String role) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.isVerified = isVerified;
        this.role = role;
    }

    public static UserShowResponseVo dtoToVo(
        ShowUserResponseDto showUserResponseDto) {
        return UserShowResponseVo.builder()
            .nickname(showUserResponseDto.getNickname())
            .email(showUserResponseDto.getEmail())
            .profileImageUrl(showUserResponseDto.getProfileImageUrl())
            .isVerified(showUserResponseDto.isVerified())
            .role(showUserResponseDto.getRole())
            .build();
    }
}
