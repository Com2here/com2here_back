package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.dto.*;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.UserShowResponseVo;
import com.com2here.com2hereback.vo.UserLoginResponseVo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

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

    @GetMapping("/show")
    public CMResponse<UserShowResponseVo> showUser() {
        try{
            ShowUserResponseDto showUserResponseDto = userService.ShowUser();
            UserShowResponseVo userShowResponseVo = UserShowResponseVo.dtoToVo(showUserResponseDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, userShowResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 정보 수정 API
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CMResponse<Void> updateUser(@ModelAttribute UserUpdateDto userUpdateDto) {
        try {
            userService.updateUser(userUpdateDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
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
