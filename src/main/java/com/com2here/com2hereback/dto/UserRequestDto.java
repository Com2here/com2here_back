package com.com2here.com2hereback.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    private int user_id;
    private String username;
    private String email;
    private String password;
    private String password_confirmation;
    private String token; // JWT 토큰

    @Builder
    public UserRequestDto(int user_id, String username, String email, String password,String password_confirmation , String token) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.token = token;
    }

}
