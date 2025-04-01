package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.EmailRequestDto;
import com.com2here.com2hereback.dto.ResetPasswordRequestDto;
import com.com2here.com2hereback.service.EmailService;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService emailService;

    // 이메일 인증코드 전송
    @PostMapping("/send")
    public ResponseEntity<?> sendAuthEmail(@RequestBody EmailRequestDto emailRequestDto) {
        try {
            CMResponse status = emailService.sendEmail(emailRequestDto.getMail());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), status.getData()));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // @GetMapping("/verify")
    // public ResponseEntity<?> verifyCode(@RequestParam String mail, @RequestParam
    // String code) {
    // try {
    // emailService.verifyCode(mail, code);
    // return ResponseEntity.status(HttpStatus.FOUND)
    // .location(URI.create("http://localhost:5173"))
    // .build();
    // } catch (Exception e) {
    // return ResponseEntity.ok().body(new
    // CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
    // }
    // }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody EmailRequestDto emailRequestDto) {
        String email = emailRequestDto.getMail();
        String code = emailRequestDto.getVerifyCode();
        try {
            CMResponse response = emailService.verifyCode(email, code);

            // 성공 시
            if (response.getCode() == BaseResponseStatus.SUCCESS.getCode()) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                // 실패 시
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 인증코드 전송 API
    // 입력값 : mail
    // 출력값 :
    @PostMapping("/authcode")
    public ResponseEntity<?> sendCodeEmail(@RequestBody EmailRequestDto emailRequestDto) {
        try {
            CMResponse status = emailService.sendCodeEmail(emailRequestDto.getMail());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(),
                    status.getMessage(), status.getData()));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 비밀번호 재설정 API
    // 입력값 :
    // 출력값 :
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        try {
            CMResponse status = emailService.resetPassword(resetPasswordRequestDto.getMail(),
                    resetPasswordRequestDto.getCode(), resetPasswordRequestDto.getPassword(),
                    resetPasswordRequestDto.getConfirmPassword());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), status.getData()));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
