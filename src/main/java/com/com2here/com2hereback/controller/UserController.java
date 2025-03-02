package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
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
        try {
            CMResponse status = userService.RegisterUser(userRequestDto);
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 로그인 API
    // 입력값 : email, password
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            CMResponse status = userService.LoginUser(userRequestDto);

            System.out.println(status.getData());
            System.out.println(status.getData());
            UserResponseVo userResponseVo = UserResponseVo.builder()
                .token((String) status.getData())
                .build();
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(),status.getMessage(), userResponseVo.getToken()));
        }
        catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 유저 조회 API
    // 입력값 : Token
    @GetMapping("/show")
    public ResponseEntity<?> showUser(HttpServletRequest request) {
        try{
            CMResponse status = userService.ShowUser(request);
            ShowUserResponseVo showUserResponseVo = ShowUserResponseVo.dtoToVo(
                (ShowUserResponseDto) status.getData());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), showUserResponseVo));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 유저 정보 수정 API
    // 입력값 : Token, userRequestDto
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto userRequestDto, HttpServletRequest request) {
        try{
            CMResponse status = userService.updateUser(userRequestDto, request);
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 유저 정보 삭제 API
    // 입력값
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserRequestDto userRequestDto, HttpServletRequest request) {
        try {
            CMResponse status = userService.deleteUser(userRequestDto, request);
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(),status.getMessage(),null));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
