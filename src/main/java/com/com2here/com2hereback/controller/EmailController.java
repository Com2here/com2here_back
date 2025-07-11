package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.dto.EmailRequestDto;
import com.com2here.com2hereback.dto.ResetPasswordRequestDto;
import com.com2here.com2hereback.service.EmailService;

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

    // 이메일 인증코드 전송
    @PostMapping("/authcode")
    public CMResponse<Void> sendAuthEmail(@RequestBody EmailRequestDto emailRequestDto) {
        try {
            emailService.sendEmail(emailRequestDto.getMail());
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify")
    public CMResponse<Void> verifyCode(@RequestBody EmailRequestDto emailRequestDto) {
        try {
            emailService.verifyCode(emailRequestDto.getMail(), emailRequestDto.getVerifyCode());
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 비밀번호 재설정 API
    // 입력값 :
    // 출력값 :
    @PostMapping("/password/reset")
    public CMResponse<Void> resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        try {
            emailService.resetPassword(resetPasswordRequestDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
