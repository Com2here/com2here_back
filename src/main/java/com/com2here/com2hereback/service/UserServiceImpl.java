package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.domain.OauthAccount;
import com.com2here.com2hereback.common.Role;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.ChgPasswordReqDto;
import com.com2here.com2hereback.dto.SocialLoginReqDto;
import com.com2here.com2hereback.dto.UserDeleteReqDto;
import com.com2here.com2hereback.dto.UserLoginReqDto;
import com.com2here.com2hereback.dto.UserShowRespDto;
import com.com2here.com2hereback.dto.UserLoginRespDto;
import com.com2here.com2hereback.dto.UserRegisterReqDto;
import com.com2here.com2hereback.dto.UserUpdateReqDto;
import com.com2here.com2hereback.repository.OauthAccountRepository;
import com.com2here.com2hereback.repository.UserRepository;
import com.com2here.com2hereback.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final OauthAccountRepository oauthAccountRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public void RegisterUser(UserRegisterReqDto userRegisterReqDto) {

        // 2602 : 비밀번호 불일치
        if (!userRegisterReqDto.getPassword().equals(userRegisterReqDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        // 2100 : 중복된 이메일
        if (userRepository.existsByEmail(userRegisterReqDto.getEmail())) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
        }

        System.out.println(userRegisterReqDto.getEmail());

        User user = User.builder()
            .nickname(userRegisterReqDto.getNickname())
            .password(bCryptPasswordEncoder.encode(userRegisterReqDto.getPassword()))
            .email(userRegisterReqDto.getEmail())
            .uuid(null)
            .isEmailVerified(false)
            .role(Role.ADMIN)
            .profileImageUrl(null)
            .createdAt(LocalDateTime.now())
            .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserLoginRespDto registerOrLoginSocialUser(SocialLoginReqDto socialLoginReqDto) {
        String email = socialLoginReqDto.getEmail();
        String nickname = socialLoginReqDto.getNickname();
        String provider = socialLoginReqDto.getProvider();
        String oauthId = socialLoginReqDto.getOauthId();
        String profileImageUrl = socialLoginReqDto.getProfileImageUrl();
        
        Optional<OauthAccount> existingOauth = oauthAccountRepository.findByProviderAndOauthId(provider, oauthId);
        User user;

        if (existingOauth.isPresent()) {
            user = existingOauth.get().getUser();
        } else {
            if (userRepository.existsByEmail(email)) {
                throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
            }

            user = User.builder()
                .nickname(nickname)
                .email(email)
                .password(null)
                .uuid(UUID.randomUUID().toString())
                .isEmailVerified(true)
                .role(Role.SOCIAL)
                .profileImageUrl(profileImageUrl)
                .createdAt(LocalDateTime.now())
                .refreshToken(null)
                .build();

            user = userRepository.save(user);

            OauthAccount oauthAccount = OauthAccount.builder()
                .user(user)
                .provider(provider)
                .oauthId(oauthId)
                .build();

            oauthAccountRepository.save(oauthAccount);
        }

        String accessToken = tokenProvider.createAccessToken(user.getUuid());
        String refreshToken = tokenProvider.createRefreshToken(user.getUuid());

        User updatedUser = User.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .password(user.getPassword())
            .uuid(user.getUuid())
            .isEmailVerified(user.isEmailVerified())
            .role(user.getRole())
            .profileImageUrl(user.getProfileImageUrl())
            .createdAt(user.getCreatedAt())
            .refreshToken(refreshToken)
            .build();

        userRepository.save(updatedUser);

        return UserLoginRespDto.entityToDto(updatedUser, accessToken, refreshToken, updatedUser.getRole().name());
    }



    @Override
    @Transactional
    public UserLoginRespDto LoginUser(UserLoginReqDto userLoginReqDto) {

        // 회원 존재 X 2106
        User user = userRepository.findByEmail(userLoginReqDto.getEmail());
        if (user == null || !bCryptPasswordEncoder.matches(userLoginReqDto.getPassword(),
            user.getPassword())) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        String accessToken = tokenProvider.createAccessToken(user.getUuid());
        String refreshToken = tokenProvider.createRefreshToken(user.getUuid());

        User updateUser = User.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .password(user.getPassword())
            .isEmailVerified(user.isEmailVerified())
            .uuid(user.getUuid())
            .role(user.getRole())
            .profileImageUrl(user.getProfileImageUrl())
            .refreshToken(refreshToken)
            .createdAt(user.getCreatedAt())
            .lastLoginAt(LocalDateTime.now())
            .build();

        userRepository.save(updateUser);

        UserLoginRespDto userLoginResponseDto = UserLoginRespDto.entityToDto(user,
            accessToken, refreshToken, user.getRole().name());

        return userLoginResponseDto;
    }

    @Override
    public UserShowRespDto ShowUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        UserShowRespDto showUserResponseDto = UserShowRespDto.entityToDto(user);
        return showUserResponseDto;
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateReqDto userUpdateReqDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        String updatedProfileImageUrl = user.getProfileImageUrl();
        if (userUpdateReqDto.getProfileImage() != null && !userUpdateReqDto.getProfileImage().isEmpty()) {
            updatedProfileImageUrl = fileStorageService.upload(userUpdateReqDto.getProfileImage());
        }

        User updatedUser = User.builder()
                .userId(user.getUserId())
                .nickname(userUpdateReqDto.getNickname())
                .email(userUpdateReqDto.getEmail())
                .profileImageUrl(updatedProfileImageUrl)
                .password(user.getPassword())
                .uuid(user.getUuid())
                .role(user.getRole())
                .refreshToken(user.getRefreshToken())
                .build();

        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UserDeleteReqDto userDeleteReqDto) {
        // 400 : 데이터 누락
        if (userDeleteReqDto == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        // 2106
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        // 2202
        if (!bCryptPasswordEncoder.matches(userDeleteReqDto.getPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MISMATCH);
        }
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void chgPassword(ChgPasswordReqDto chgPasswordReqDto) {

        // 사용자 조회
        User user = userRepository.findByEmail(chgPasswordReqDto.getEmail());
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        // 현재 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(chgPasswordReqDto.getCurrentPassword(),
            user.getPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_CURRENT_PASSWORD);
        }

        // 2602 : 비밀번호 불일치
        if (!chgPasswordReqDto.getNewPassword()
            .equals(chgPasswordReqDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        user = User.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .password(bCryptPasswordEncoder.encode(chgPasswordReqDto.getNewPassword()))
            .email(user.getEmail())
            .uuid(user.getUuid())
            .role(user.getRole())
            .profileImageUrl(user.getProfileImageUrl())
            .refreshToken(user.getRefreshToken())
            .isEmailVerified(user.isEmailVerified())
            .build();

        userRepository.save(user);
    }
}
