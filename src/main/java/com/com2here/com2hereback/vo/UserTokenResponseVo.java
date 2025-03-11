package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.UserTokenResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserTokenResponseVo {

    private String accessToken;
    private String refreshToken;

    @Builder
    public UserTokenResponseVo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public static UserTokenResponseVo dtoToVo(UserTokenResponseDto userTokenResponseDto) {
        return UserTokenResponseVo.builder()
            .accessToken(userTokenResponseDto.getAccessToken())
            .refreshToken(userTokenResponseDto.getRefreshToken())
            .build();
    }
}

