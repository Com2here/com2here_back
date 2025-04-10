package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ChgPasswordRequestDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserLoginResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserTokenResponseDto;

public interface UserService {

    void RegisterUser(UserRequestDto userRequestDto);
    UserLoginResponseDto LoginUser(UserRequestDto userRequestDto);
    ShowUserResponseDto ShowUser();
    void updateUser(UserRequestDto userRequestDto);
    void deleteUser(UserRequestDto userRequestDto);
    void chgPassword(ChgPasswordRequestDto chgPasswordRequestDto);
    UserTokenResponseDto rotateToken();
}
