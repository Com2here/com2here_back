package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserResponseDto;
import com.com2here.com2hereback.repository.UserRepository;
import com.com2here.com2hereback.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public CMResponse RegisterUser(UserRequestDto userRequestDto) {
        BaseResponseStatus status;

        // 400 : 데이터 누락
        if (userRequestDto == null || userRequestDto.getPassword() == null || userRequestDto.getEmail() == null) {
            status = BaseResponseStatus.WRONG_PARAM;
            return CMResponse.fail(status.getCode(),status,null);
        }

        // 400 : 비밀번호 형식 불일치
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        if (!(userRequestDto.getPassword()).matches(regex)) {
            status = BaseResponseStatus.PASSWORD_FORMAT_INVALID;
            return CMResponse.fail(status.getCode(),status,null);
        }

        // 중복된 이메일
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            status = BaseResponseStatus.DUPLICATE_EMAIL;
            return CMResponse.fail(status.getCode(),status,null);
        }

        User user = User.builder()
            .username(userRequestDto.getUsername())
            .password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
            .email(userRequestDto.getEmail()).build();

        userRepository.save(user);

        status = BaseResponseStatus.REGISTRATION_SUCCESS;

        return CMResponse.success(status.getCode(),status,null);
    }

    @Override
    public CMResponse LoginUser(UserRequestDto userRequestDto) {
        BaseResponseStatus status;

        // 400 : 데이터 누락
        if(userRequestDto.getEmail()==null || userRequestDto.getPassword()==null){
            status = BaseResponseStatus.WRONG_PARAM;
            return CMResponse.fail(status.getCode(),status);
        }

        // 회원 존재 X 2106
        User user = userRepository.findByEmail(userRequestDto.getEmail());
        if(user==null || !bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())){
            status = BaseResponseStatus.NO_EXIST_MEMBERS;
            return CMResponse.fail(status.getCode(),status);
        }
        String token = tokenProvider.create(user);

        status = BaseResponseStatus.LOGIN_SUCCESS;

        return CMResponse.success(status.getCode(),status,token);
    }

    @Override
    public CMResponse ShowUser(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = (int) authentication.getPrincipal(); // userId를 가져옵니다.

        User user = userRepository.findById(userId);
        if (user == null) {
            // return BaseResponseStatus.NO_EXIST_MEMBERS; // 존재하지 않는 사용자
            return CMResponse.fail(2106,BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        ShowUserResponseDto showUserResponseDto = ShowUserResponseDto.entityToDto(user);
        return CMResponse.success(200,BaseResponseStatus.USER_INFO_RETRIEVED,showUserResponseDto);
    }

    @Override
    @Transactional
    public CMResponse updateUser(UserRequestDto userRequestDto, HttpServletRequest request) {
        BaseResponseStatus status;

        // 400 : 데이터 누락
        if(userRequestDto==null){
            status = BaseResponseStatus.WRONG_PARAM;
            return CMResponse.fail(status.getCode(),status);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = (int) authentication.getPrincipal(); // userId를 가져옵니다.

        User user = userRepository.findById(userId);
        User updatedUser = User.builder()
            .user_id(user.getUser_id())
            .username(userRequestDto.getUsername() != null ? userRequestDto.getUsername() : user.getUsername())
            .email(userRequestDto.getEmail() != null ? userRequestDto.getEmail() : user.getEmail())
            .password(user.getPassword())
            .build();

        userRepository.save(updatedUser);

        status = BaseResponseStatus.MEMBER_INFO_UPDATED;
        return CMResponse.success(status.getCode(),status,null);
    }

    @Override
    @Transactional
    public CMResponse deleteUser(UserRequestDto userRequestDto, HttpServletRequest request) {
        BaseResponseStatus status;

        // 400 : 데이터 누락
        if(userRequestDto==null){
            status = BaseResponseStatus.WRONG_PARAM;
            return CMResponse.fail(status.getCode(),status);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = (int) authentication.getPrincipal(); // userId를 가져옵니다.

        // 2106
        User user = userRepository.findById(userId);
        if (user == null) {
            status =  BaseResponseStatus.NO_EXIST_MEMBERS; // 존재하지 않는 사용자
            return CMResponse.fail(status.getCode(),status);
        }
        // 2202
        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            status = BaseResponseStatus.PASSWORD_MISMATCH; // 비밀번호 불일치
            return CMResponse.fail(status.getCode(),status);
        }

        userRepository.delete(user);
        status = BaseResponseStatus.MEMBER_DELETE_SUCCESS;

        // 200
        return CMResponse.success(status.getCode(),status,null);
    }


}
