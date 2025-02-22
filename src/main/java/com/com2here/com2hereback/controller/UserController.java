package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.dto.CMRespDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserResponseDto;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.UserResponseVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    // 회원가입 API
    // 입력값 : userRequestDto
    // 반환값 : String
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            String message = userService.RegisterUser(userRequestDto);

            return ResponseEntity.ok().body(new CMRespDto<>(1, message, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 처리 중 오류가 발생했습니다.");
        }
    }

    // 로그인 API
    // 입력값 :
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto userResponseDto = userService.LoginUser(userRequestDto);

            UserResponseVo userResponseVo = UserResponseVo.builder()
                .message(userResponseDto.getMessage())
                .token(userResponseDto.getToken())
                .build();
            return ResponseEntity.ok().body(new CMRespDto<>(1,userResponseVo.getMessage(), userResponseVo.getToken()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("로그인 처리 중 오류가 발생했습니다.");
        }
    }
}
