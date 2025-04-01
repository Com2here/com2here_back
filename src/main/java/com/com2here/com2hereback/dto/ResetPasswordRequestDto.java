package com.com2here.com2hereback.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {

    private String mail;
    private String code;
    private String password;
    private String confirmPassword;
}
