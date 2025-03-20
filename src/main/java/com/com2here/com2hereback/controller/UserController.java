package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.ChgPasswordRequestDto;
import com.com2here.com2hereback.dto.EmailRequestDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserTokenResponseDto;
import com.com2here.com2hereback.service.EmailService;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.ShowUserResponseVo;
import com.com2here.com2hereback.vo.UserTokenResponseVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    // 회원가입 API
    // 입력값 : userRequestDto
    // 반환값 : code, message
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
    // 입력값 : username, email, password, password_confirmation
    // 반환값 : code, message, Access Token, Refresh Token
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            CMResponse status = userService.LoginUser(userRequestDto);

            UserTokenResponseDto userTokenResponseDto = UserTokenResponseDto.entityToDto(String.valueOf(((UserTokenResponseDto) status.getData()).getAccessToken()), String.valueOf(((UserTokenResponseDto) status.getData()).getRefreshToken()));
            UserTokenResponseVo userTokenResponseVo = UserTokenResponseVo.dtoToVo(userTokenResponseDto);

            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(),status.getMessage(),
                userTokenResponseVo));
        }
        catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 유저 조회 API
    // 입력값 : Token
    // 출력값 : code, message, showUserResponseVo
    @GetMapping("/show")
    public ResponseEntity<?> showUser() {
        try{
            CMResponse status = userService.ShowUser();
            ShowUserResponseVo showUserResponseVo = ShowUserResponseVo.dtoToVo((ShowUserResponseDto) status.getData());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), showUserResponseVo));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 유저 정보 수정 API
    // 입력값 : Token, userRequestDto
    // 출력값 : code, message, null
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto userRequestDto) {
        try{
            CMResponse status = userService.updateUser(userRequestDto);
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 유저 정보 삭제 API
    // 입력값 : password
    // 출력값 : code, message, null
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            CMResponse status = userService.deleteUser(userRequestDto);
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(),status.getMessage(),null));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 비밀번호 변경 API
    // 입력값 : currentPassword, password, confirmPassword
    // 출력값 : code, message, null
    @PatchMapping("/password/change")
    public ResponseEntity<?> chgPassword(@RequestBody ChgPasswordRequestDto chgPasswordRequestDto) {
        try{
            CMResponse status = userService.chgPassword(chgPasswordRequestDto);
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
