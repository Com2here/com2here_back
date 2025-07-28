package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import com.com2here.com2hereback.common.Role;

@Data
public class AdminUserUpdateReqDto {

    private String nickname;

    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

    private Role role;

    private MultipartFile profileImage;
}
