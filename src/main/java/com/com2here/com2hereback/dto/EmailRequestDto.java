package com.com2here.com2hereback.dto;

import lombok.Data;

@Data
public class EmailRequestDto {
    // 이메일 주소
    private String mail;
    // 인증 코드
    private String verifyCode;
}
