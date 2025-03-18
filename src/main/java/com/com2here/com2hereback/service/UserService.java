package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.UserRequestDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    // 회원가입
    CMResponse RegisterUser(UserRequestDto userRequestDto);

    // 로그인
    CMResponse LoginUser(UserRequestDto userRequestDto);

    // 조회
    CMResponse ShowUser(HttpServletRequest request);

    // 수정
    CMResponse updateUser(UserRequestDto userRequestDto, HttpServletRequest request);

    // 삭제
    CMResponse deleteUser(UserRequestDto userRequestDto, HttpServletRequest request);
}
