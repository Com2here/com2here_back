package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.*;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.UserShowVO;

import jakarta.validation.Valid;

import com.com2here.com2hereback.vo.UserLoginVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public CMResponse<Void> registerUser(@Valid @RequestBody UserRegisterReqDto userRegisterReqDto) {
        userService.RegisterUser(userRegisterReqDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/login")
    public CMResponse<UserLoginVO> loginUser(@Valid @RequestBody UserLoginReqDto userLoginReqDto) {
        UserLoginRespDto userLoginRespDto = userService.LoginUser(userLoginReqDto);
        UserLoginVO userLoginVo = UserLoginVO.from(userLoginRespDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS, userLoginVo);
    }

    @GetMapping("/show")
    public CMResponse<UserShowVO> showUser() {
        UserShowRespDto showUserRespDto = userService.ShowUser();
        UserShowVO userShowRespVo = UserShowVO.from(showUserRespDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS, userShowRespVo);
    }

    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CMResponse<Void> updateUser(@Valid @ModelAttribute UserUpdateReqDto userUpdateDto) {
        userService.updateUser(userUpdateDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping("/delete")
    public CMResponse<Void> deleteUser(@Valid @RequestBody UserDeleteReqDto userDeleteReqDto) {
        userService.deleteUser(userDeleteReqDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @PatchMapping("/password/change")
    public CMResponse<Void> chgPassword(@Valid @RequestBody ChgPasswordReqDto chgPasswordReqDto) {
        userService.chgPassword(chgPasswordReqDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }
}
