package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowUserResponseDto {
    private String username;
    private String email;
    private boolean isVerified;

    public static ShowUserResponseDto entityToDto(User user){
        return ShowUserResponseDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .isVerified(user.isEmailVerified())
            .build();
    }
}
