package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.EmailRequestDto;
import com.com2here.com2hereback.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> sendCode(@RequestBody EmailRequestDto emailRequestDto){
        try {
            CMResponse status = emailService.sendEmail(emailRequestDto.getMail());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(),status.getMessage(),status.getData()));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
        try {
            CMResponse status = emailService.verifyCode(email, code);
            System.out.println(status.getMessage());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), status.getData()));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }


}
