package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserUpdateReqDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    private MultipartFile profileImage;

    @Builder
    public UserUpdateReqDto(String nickname, String email, MultipartFile profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
    }
}
