package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.ShowUserResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowUserResponseVo {
    private String username;
    private String email;

    @Builder
    public ShowUserResponseVo(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public static ShowUserResponseVo dtoToVo(
        ShowUserResponseDto showUserResponseDto) {
        return ShowUserResponseVo.builder()
            .username(showUserResponseDto.getUsername())
            .email(showUserResponseDto.getEmail())
            .build();
    }
}
