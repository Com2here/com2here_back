package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.UserLoginResponseDto;
import com.com2here.com2hereback.dto.UserTokenResponseDto;
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

    @Builder
    public UserLoginResponseVo(String accessToken, String refreshToken, String nickname, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.email = email;
    }


    public static UserLoginResponseVo dtoToVo(UserLoginResponseDto userLoginResponseDto) {
        return UserLoginResponseVo.builder()
            .accessToken(userLoginResponseDto.getAccessToken())
            .refreshToken(userLoginResponseDto.getRefreshToken())
            .nickname(userLoginResponseDto.getNickname())
            .email(userLoginResponseDto.getEmail())
            .build();
    }
}
