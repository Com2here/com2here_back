package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.CMResponse;

public interface EmailService {

    // 인증코드 생성
    CMResponse createCode();

    // 이메일 내용 초기화
    CMResponse setEmail(String email, String code);

    // 이메일 폼 생성
    CMResponse createEmailForm(String email);

    // 인증메일 발송
    CMResponse sendEmail(String string);


    CMResponse sendCodeEmail(String email);

    // 인증메일 검증
    CMResponse verifyCode(String email, String code);

    // 인증코드로 비밀번호 재설정
    CMResponse resetPassword(String email, String code, String newPassword, String confirmPassword);
}
