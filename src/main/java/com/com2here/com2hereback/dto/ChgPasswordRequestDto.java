package com.com2here.com2hereback.dto;

import lombok.Data;

@Data
public class ChgPasswordRequestDto {

    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
