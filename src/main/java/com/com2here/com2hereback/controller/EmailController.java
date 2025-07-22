package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.EmailAuthReqDto;
import com.com2here.com2hereback.dto.EmailVerifyReqDto;
import com.com2here.com2hereback.dto.ResetPasswordReqDto;
import com.com2here.com2hereback.service.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/code")
    public CMResponse<Void> sendAuthCodeEmail(@Valid @RequestBody EmailAuthReqDto emailAuthReqDto) {
        emailService.sendEmail(emailAuthReqDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/code/verify")
    public CMResponse<Void> verifyEmailAuthCode(@Valid @RequestBody EmailVerifyReqDto emailVerifyReqDto) {
        emailService.verifyCode(emailVerifyReqDto.getEmail(), emailVerifyReqDto.getVerifyCode());
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/password/reset")
    public CMResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordReqDto resetPasswordRequestDto) {
        emailService.resetPassword(resetPasswordRequestDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }
}
