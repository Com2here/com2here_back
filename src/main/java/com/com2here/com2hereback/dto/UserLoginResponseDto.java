package com.com2here.com2hereback.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String email;

    public static UserLoginResponseDto entityToDto(String accessToken, String refreshToken, String username, String email) {
        return UserLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .username(username)
            .email(email)
            .build();
    }
}
