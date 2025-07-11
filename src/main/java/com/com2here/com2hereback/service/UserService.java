package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ChgPasswordRequestDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserLoginResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserTokenResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void RegisterUser(UserRequestDto userRequestDto);
    UserLoginResponseDto LoginUser(UserRequestDto userRequestDto);
    ShowUserResponseDto ShowUser();
    void updateUser(String nickname, String email, MultipartFile profileImage);
    void deleteUser(UserRequestDto userRequestDto);
    void chgPassword(ChgPasswordRequestDto chgPasswordRequestDto);
    UserTokenResponseDto rotateToken();
    UserLoginResponseDto registerOrLoginSocialUser(String email, String nickname, String provider, String oauthId, String profileImageUrl);
}
