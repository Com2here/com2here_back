package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ChgPasswordReqDto;
import com.com2here.com2hereback.dto.SocialLoginReqDto;
import com.com2here.com2hereback.dto.UserDeleteReqDto;
import com.com2here.com2hereback.dto.UserLoginReqDto;
import com.com2here.com2hereback.dto.UserShowRespDto;
import com.com2here.com2hereback.dto.UserLoginRespDto;
import com.com2here.com2hereback.dto.UserRegisterReqDto;
import com.com2here.com2hereback.dto.UserUpdateReqDto;

public interface UserService {

    void RegisterUser(UserRegisterReqDto userRegisterReqDto);
    UserLoginRespDto LoginUser(UserLoginReqDto userLoginReqDto);
    UserShowRespDto ShowUser();
    void updateUser(UserUpdateReqDto userUpdateReqDto);
    void deleteUser(UserDeleteReqDto userDeleteReqDto);
    void chgPassword(ChgPasswordReqDto chgPasswordRequestDto);
    UserLoginRespDto registerOrLoginSocialUser(SocialLoginReqDto socialLoginReqDto);
}