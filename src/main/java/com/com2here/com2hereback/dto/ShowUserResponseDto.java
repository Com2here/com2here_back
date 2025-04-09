package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowUserResponseDto {
    private String nickname;
    private String email;
    private String profileImageUrl;
    private boolean isVerified;

    public static ShowUserResponseDto entityToDto(User user){
        return ShowUserResponseDto.builder()
            .nickname(user.getNickname())
            .email(user.getEmail())
            .profileImageUrl(user.getProfileImageUrl())
            .isVerified(user.isEmailVerified())
            .build();
    }
}
