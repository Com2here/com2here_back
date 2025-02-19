package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.dto.UserDTO;
import com.com2here.com2hereback.domain.UserEntity;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.security.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 비밀번호 강도 검사
    private boolean isPasswordStrong(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        return password.matches(regex);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO == null || userDTO.getPassword() == null || userDTO.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일과 비밀번호를 입력하세요.");
            }

            if (!isPasswordStrong(userDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.");
            }

            if (userService.existsByEmail(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 이메일입니다.");
            }

            UserEntity user = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .email(userDTO.getEmail()).build();

            UserEntity registeredUser = userService.create(user);

            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .email(registeredUser.getEmail())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseUserDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 처리 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/signin")
    @Operation(summary = "사용자 로그인", description = "이메일과 비밀번호로 로그인하여 인증 토큰을 받습니다.")
    public ResponseEntity<?> authenticate(
            @Parameter(description = "사용자의 이메일 주소", schema = @Schema(example = "{\"password\": \"1234\",\"email\": \"kim1@example.com\"}")) @RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getEmail() == null || userDTO.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일과 비밀번호를 입력하세요.");
            }

            UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

            if (user != null) {
                final String token = tokenProvider.create(user);

                final UserDTO responseUserDTO = UserDTO.builder()
                        .username(user.getUsername())
                        .id(user.getId())
                        .token(token)
                        .email(user.getEmail())
                        .build();
                return ResponseEntity.ok().body(responseUserDTO);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("이메일 또는 비밀번호가 잘못되었습니다.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("로그인 처리 중 오류가 발생했습니다.");
        }
    }
}
