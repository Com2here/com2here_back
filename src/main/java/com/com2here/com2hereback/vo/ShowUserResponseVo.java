package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.ShowUserResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowUserResponseVo {
    private String username;
    private String email;
    boolean isVerified;

    @Builder
    public ShowUserResponseVo(String username, String email, boolean isVerified) {
        this.username = username;
        this.email = email;
        this.isVerified = isVerified;
    }

    public static ShowUserResponseVo dtoToVo(
        ShowUserResponseDto showUserResponseDto) {
        return ShowUserResponseVo.builder()
            .username(showUserResponseDto.getUsername())
            .email(showUserResponseDto.getEmail())
            .isVerified(showUserResponseDto.isVerified())
            .build();
    }
}
