package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.ShowUserResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowUserResponseVo {
    private String nickname;
    private String email;

    @Builder
    public ShowUserResponseVo(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public static ShowUserResponseVo dtoToVo(
        ShowUserResponseDto showUserResponseDto) {
        return ShowUserResponseVo.builder()
            .nickname(showUserResponseDto.getNickname())
            .email(showUserResponseDto.getEmail())
            .build();
    }
}
