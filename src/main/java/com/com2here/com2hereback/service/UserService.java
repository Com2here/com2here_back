package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.ChgPasswordRequestDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserLoginResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;

public interface UserService {

    // 회원가입
    void RegisterUser(UserRequestDto userRequestDto);

    // 로그인
    UserLoginResponseDto LoginUser(UserRequestDto userRequestDto);

    // 조회
    ShowUserResponseDto ShowUser();

    // 수정
    void updateUser(UserRequestDto userRequestDto);

    // 삭제
    void deleteUser(UserRequestDto userRequestDto);

    // 비밀번호 변경
    void chgPassword(ChgPasswordRequestDto chgPasswordRequestDto);

}
