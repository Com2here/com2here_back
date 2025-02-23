package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.config.exception.InvalidCredentialsException;
import com.com2here.com2hereback.dto.CMRespDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserResponseDto;
import com.com2here.com2hereback.repository.UserRepository;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.ShowUserResponseVo;
import com.com2here.com2hereback.vo.UserResponseVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 회원가입 API
    // 입력값 : userRequestDto
    // 반환값 : String
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto userRequestDto) {
        // Dto 유무, 비밀번호 유무, 이메일 유무 확인
        if (userRequestDto == null || userRequestDto.getPassword() == null || userRequestDto.getEmail() == null) {
            return ResponseEntity.ok().body(new CMRespDto<>(400, "이메일과 비밀번호를 입력하세요.",null));
        }

        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        if (!(userRequestDto.getPassword()).matches(regex)) {
            return ResponseEntity.ok().body(new CMRespDto<>(400, "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.",null));
        }

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            return ResponseEntity.ok().body(new CMRespDto<>(409, "이미 등록된 이메일입니다.",null));
        }
        try {
            int code = userService.RegisterUser(userRequestDto);

            return ResponseEntity.ok().body(new CMRespDto<>(code, "회원가입 성공", null));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMRespDto<>(500, "서버 오류가 발생했습니다. 잠시 후 다시 시도하세요.",null));
        }
    }

    // 로그인 API
    // 입력값 : email, password
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequestDto userRequestDto) {
        if (userRequestDto.getEmail() == null || userRequestDto.getPassword() == null) {
            return ResponseEntity.ok().body(new CMRespDto<>(400, "이메일과 비밀번호를 입력하세요.",null));
        }
        try {
            UserResponseDto userResponseDto = userService.LoginUser(userRequestDto);

            UserResponseVo userResponseVo = UserResponseVo.builder()
                .message(userResponseDto.getMessage())
                .token(userResponseDto.getToken())
                .build();
            return ResponseEntity.ok().body(new CMRespDto<>(200,userResponseVo.getMessage(), userResponseVo.getToken()));

        } catch (InvalidCredentialsException e){
            // 로그인 실패 시
            return ResponseEntity.ok().body(new CMRespDto<>(401, "이메일 또는 비밀번호가 잘못되었습니다.", null));
        }
        catch (Exception e) {
            return ResponseEntity.ok().body(new CMRespDto<>(500, "서버 오류가 발생했습니다. 잠시 후 다시 시도하세요.", null));
        }
    }

    // 유저 조회 API
    // 입력값 : Token
    @GetMapping("/show")
    public ResponseEntity<?> showUser(HttpServletRequest request) {
        try{
            ShowUserResponseDto showUserResponseDto = userService.ShowUser(request);
            ShowUserResponseVo showUserResponseVo = ShowUserResponseVo.dtoToVo(showUserResponseDto);
            return ResponseEntity.ok().body(new CMRespDto<>(200, "유저 조회 성공", showUserResponseVo));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new CMRespDto<>(500, "서버 오류가 발생했습니다. 잠시 후 다시 시도하세요.", null));
        }
    }

    // 유저 정보 수정 API
    // 입력값 : Token, userRequestDto
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto userRequestDto, HttpServletRequest request) {
        try{
            userService.updateUser(userRequestDto, request);
            return ResponseEntity.ok().body(new CMRespDto<>(200, "정보 수정 성공", null));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new CMRespDto<>(500, "서버 오류가 발생했습니다. 잠시 후 다시 시도하세요.", null));
        }
    }

    // 유저 정보 삭제 API
    // 입력값
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserRequestDto userRequestDto, HttpServletRequest request) {
        try {
            int code = userService.deleteUser(userRequestDto, request);
            return ResponseEntity.ok().body(new CMRespDto<>(code, "삭제 성공", null));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMRespDto<>(500, "서버 오류가 발생했습니다. 잠시 후 다시 시도하세요.", null));
        }
    }
}
