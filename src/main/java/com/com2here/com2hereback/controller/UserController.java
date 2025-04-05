package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.common.exception.BaseException;
import com.com2here.com2hereback.dto.ChgPasswordRequestDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserLoginResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserTokenResponseDto;
import com.com2here.com2hereback.service.EmailService;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.ShowUserResponseVo;
import com.com2here.com2hereback.vo.UserLoginResponseVo;
import com.com2here.com2hereback.vo.UserTokenResponseVo;
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
    public CMResponse<Void> registerUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            userService.RegisterUser(userRequestDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인 API
    // 입력값 : username, email, password, password_confirmation
    // 반환값 : code, message, Access Token, Refresh Token
    @PostMapping("/login")
    public CMResponse<UserLoginResponseVo> loginUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            UserLoginResponseDto userLoginResponseDto = userService.LoginUser(userRequestDto);
            UserLoginResponseVo userLoginResponseVo = UserLoginResponseVo.dtoToVo(userLoginResponseDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, userLoginResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 조회 API
    // 입력값 : Token
    // 출력값 : code, message, showUserResponseVo
    @GetMapping("/show")
    public CMResponse<ShowUserResponseVo> showUser() {
        try{
            ShowUserResponseDto showUserResponseDto = userService.ShowUser();
            ShowUserResponseVo showUserResponseVo = ShowUserResponseVo.dtoToVo(showUserResponseDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, showUserResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 정보 수정 API
    // 입력값 : Token, userRequestDto
    // 출력값 : code, message, null
    @PatchMapping("/update")
    public CMResponse<Void> updateUser(@RequestBody UserRequestDto userRequestDto) {
        try{
            userService.updateUser(userRequestDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 정보 삭제 API
    // 입력값 : password
    // 출력값 : code, message, null
    @DeleteMapping("/delete")
    public CMResponse<Void> deleteUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            userService.deleteUser(userRequestDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 비밀번호 변경 API
    // 입력값 : currentPassword, password, confirmPassword
    // 출력값 : code, message, null
    @PatchMapping("/password/change")
    public CMResponse<Void> chgPassword(@RequestBody ChgPasswordRequestDto chgPasswordRequestDto) {
        try{
            userService.chgPassword(chgPasswordRequestDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
