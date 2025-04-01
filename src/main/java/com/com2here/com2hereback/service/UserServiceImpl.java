package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.ChgPasswordRequestDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserLoginResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserTokenResponseDto;
import com.com2here.com2hereback.repository.UserRepository;
import com.com2here.com2hereback.config.jwt.TokenProvider;
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
        if (userRequestDto == null || userRequestDto.getPassword() == null || userRequestDto.getEmail() == null || userRequestDto.getConfirmPassword() == null) {
            status = BaseResponseStatus.WRONG_PARAM;
            return CMResponse.fail(status.getCode(),status,null);
        }

        // 2601 : 비밀번호 형식 불일치
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        if (!(userRequestDto.getPassword()).matches(regex)) {
            status = BaseResponseStatus.PASSWORD_FORMAT_INVALID;
            return CMResponse.fail(status.getCode(),status,null);
        }

        // 2602 : 비밀번호 불일치
        if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
            status = BaseResponseStatus.UNMATCHED_PASSWORD;
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
            .email(userRequestDto.getEmail())
            .uuid(null)
            .isEmailVerified(false)
            .role(false)
            .profileImageUrl(null)
            .build();


        userRepository.save(user);

        status = BaseResponseStatus.REGISTRATION_SUCCESS;

        return CMResponse.success(status.getCode(),status,null);
    }

    @Override
    @Transactional
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
        String accessToken = tokenProvider.createAccessToken(user.getUuid());
        String refreshToken = tokenProvider.createRefreshToken(user.getUuid());

        // refreshToken 추가
        User updateUser = User.builder()
            .user_id(user.getUser_id())
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .uuid(user.getUuid())
            .refreshToken(refreshToken)
            .build();

        userRepository.save(updateUser);

        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .username(user.getUsername())
            .email(user.getEmail())
            .build();

        status = BaseResponseStatus.LOGIN_SUCCESS;
        return CMResponse.success(status.getCode(),status, userLoginResponseDto);
    }

    @Override
    public CMResponse ShowUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal(); // uuid를 가져옵니다.

        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            return CMResponse.fail(2106,BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        ShowUserResponseDto showUserResponseDto = ShowUserResponseDto.entityToDto(user);
        return CMResponse.success(200,BaseResponseStatus.USER_INFO_RETRIEVED,showUserResponseDto);
    }

    @Override
    @Transactional
    public CMResponse updateUser(UserRequestDto userRequestDto) {
        BaseResponseStatus status;

        // 400 : 데이터 누락
        if(userRequestDto==null){
            status = BaseResponseStatus.WRONG_PARAM;
            return CMResponse.fail(status.getCode(),status);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal(); // uuid를 가져옵니다.

        User user = userRepository.findByUuid(uuid);
        User updatedUser = User.builder()
            .user_id(user.getUser_id())
            .username(userRequestDto.getUsername() != null ? userRequestDto.getUsername() : user.getUsername())
            .email(userRequestDto.getEmail() != null ? userRequestDto.getEmail() : user.getEmail())
            .password(user.getPassword())
            .uuid(user.getUuid())
            .refreshToken(user.getRefreshToken())
            .build();

        userRepository.save(updatedUser);

        status = BaseResponseStatus.MEMBER_INFO_UPDATED;
        return CMResponse.success(status.getCode(),status,null);
    }

    @Override
    @Transactional
    public CMResponse deleteUser(UserRequestDto userRequestDto) {
        BaseResponseStatus status;

        // 400 : 데이터 누락
        if(userRequestDto==null){
            status = BaseResponseStatus.WRONG_PARAM;
            return CMResponse.fail(status.getCode(),status);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal(); // uuid를 가져옵니다.

        // 2106
        User user = userRepository.findByUuid(uuid);
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

    @Override
    @Transactional
    public CMResponse chgPassword(ChgPasswordRequestDto chgPasswordRequestDto) {
        BaseResponseStatus status;

        // 400 : 데이터 누락
        if (chgPasswordRequestDto == null || chgPasswordRequestDto.getCurrentPassword() == null ||
            chgPasswordRequestDto.getNewPassword() == null ||
            chgPasswordRequestDto.getEmail() == null ||
            chgPasswordRequestDto.getConfirmPassword() == null) {

            status = BaseResponseStatus.WRONG_PARAM;
            return CMResponse.fail(status.getCode(), status, null);
        }

        // 2601 : 비밀번호 형식 불일치
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        if (!(chgPasswordRequestDto.getNewPassword()).matches(regex)) {
            status = BaseResponseStatus.PASSWORD_FORMAT_INVALID;
            return CMResponse.fail(status.getCode(), status, null);
        }

        // 2602 : 비밀번호 불일치
        if (!chgPasswordRequestDto.getNewPassword().equals(chgPasswordRequestDto.getConfirmPassword())) {
            status = BaseResponseStatus.UNMATCHED_PASSWORD;
            return CMResponse.fail(status.getCode(), status, null);
        }

        // 사용자 조회
        User user = userRepository.findByEmail(chgPasswordRequestDto.getEmail());
        if (user == null) {
            status = BaseResponseStatus.NO_EXIST_MEMBERS;
            return CMResponse.fail(status.getCode(), status, null);
        }

        // 현재 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(chgPasswordRequestDto.getCurrentPassword(), user.getPassword())) {
            status = BaseResponseStatus.INVALID_CURRENT_PASSWORD;
            return CMResponse.fail(status.getCode(), status, null);
        }

        user = User.builder()
            .user_id(user.getUser_id())
            .username(user.getUsername())
            .password(bCryptPasswordEncoder.encode(chgPasswordRequestDto.getNewPassword()))
            .email(user.getEmail())
            .uuid(user.getUuid())
            .isEmailVerified(user.isEmailVerified())
            .build();

        userRepository.save(user);

        status = BaseResponseStatus.PASSWORD_CHANGE_SUCCESS;
        return CMResponse.success(status.getCode(), status, null);
    }

}
