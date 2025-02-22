package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserResponseDto;

public interface UserService {

    // 회원가입
    String RegisterUser(UserRequestDto userRequestDto);
    // 로그인
    UserResponseDto LoginUser(UserRequestDto userRequestDto);
}
