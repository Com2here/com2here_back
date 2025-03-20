package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.UserRequestDto;

public interface UserService {

    // 회원가입
    CMResponse RegisterUser(UserRequestDto userRequestDto);

    // 로그인
    CMResponse LoginUser(UserRequestDto userRequestDto);

    // 조회
    CMResponse ShowUser();

    // 수정
    CMResponse updateUser(UserRequestDto userRequestDto);

    // 삭제
    CMResponse deleteUser(UserRequestDto userRequestDto);

}
