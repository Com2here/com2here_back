package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.EmailAuthReqDto;
import com.com2here.com2hereback.dto.ResetPasswordReqDto;
import jakarta.mail.internet.MimeMessage;

public interface EmailService {

    String createCode();
    String setEmail(String email, String code);
    MimeMessage createEmailForm(String email);
    void sendEmail(EmailAuthReqDto emailAuthReqDto) ;
    void verifyCode(String email, String code);
    void resetPassword(ResetPasswordReqDto resetPasswordRequestDto);
}
