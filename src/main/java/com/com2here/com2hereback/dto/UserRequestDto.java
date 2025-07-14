package com.com2here.com2hereback.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    private Long userId;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String password;
    private String confirmPassword;
    private String token;

    @Builder
    public UserRequestDto(Long userId, String nickname, String email, String profileImageUrl, String password,String confirmPassword, String token) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.token = token;
    }

}
