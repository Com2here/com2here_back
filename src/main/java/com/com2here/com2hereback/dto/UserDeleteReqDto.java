package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDeleteReqDto {

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]).{8,20}$",
        message = "비밀번호는 8~20자이며 영문, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;

    @Builder
    public UserDeleteReqDto(String password) {
        this.password = password;
    }
}
