package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.CMResponse;

public interface EmailService {

    // 인증코드 생성
    CMResponse createCode();

    // 이메일 내용 초기화
    CMResponse setEmail(String email, String code);

    // 이메일 폼 생성
    CMResponse createEmailForm(String email);

    // 인증코드 발송
    CMResponse sendEmail(String string);

    // 인증코드 검증
    CMResponse verifyCode(String email, String code);
}
