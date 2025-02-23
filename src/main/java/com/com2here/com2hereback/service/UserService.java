package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.CMRespDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    // 회원가입
    int RegisterUser(UserRequestDto userRequestDto);
    // 로그인
    UserResponseDto LoginUser(UserRequestDto userRequestDto);
    // 조회
    ShowUserResponseDto ShowUser(HttpServletRequest request);
    // 수정
    void updateUser(UserRequestDto userRequestDto, HttpServletRequest request);
    // 삭제
    int deleteUser(UserRequestDto userRequestDto, HttpServletRequest request);
}
