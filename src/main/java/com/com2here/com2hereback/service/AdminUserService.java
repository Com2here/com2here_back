package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.AdminUserRegisterReqDto;
import com.com2here.com2hereback.dto.AdminUserUpdateReqDto;
import com.com2here.com2hereback.dto.UserShowRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    Page<UserShowRespDto> getAllUsers(Pageable pageable);
    void createUserByAdmin(AdminUserRegisterReqDto registerDto);
    void updateUserByAdmin(String uuid, AdminUserUpdateReqDto updateDto);
    void deleteUserByAdmin(String uuid);
}
