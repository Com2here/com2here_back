package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.domain.OauthAccount;
import com.com2here.com2hereback.domain.Role;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.ChgPasswordRequestDto;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserLoginResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserTokenResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

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
    private final FileUploadService fileUploadService;

    @Override
    @Transactional
    public void RegisterUser(UserRequestDto userRequestDto) {

        // 400 : 데이터 누락
        if (userRequestDto == null || userRequestDto.getPassword() == null
            || userRequestDto.getEmail() == null || userRequestDto.getConfirmPassword() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 2601 : 비밀번호 형식 불일치
        if (!(userRequestDto.getPassword()).matches(
            "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]).{8,20}$")) {
            throw new BaseException(BaseResponseStatus.PASSWORD_FORMAT_INVALID);
        }

        // 2602 : 비밀번호 불일치
        if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        // 2100 : 중복된 이메일
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
        }

        User user = User.builder()
            .nickname(userRequestDto.getNickname())
            .password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
            .email(userRequestDto.getEmail())
            .uuid(null)
            .isEmailVerified(false)
            .role(Role.USER)
            .profileImageUrl(null)
            .createdAt(LocalDateTime.now())
            .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserLoginResponseDto registerOrLoginSocialUser(String email, String nickname, String provider, String oauthId, String profileImageUrl) {
        // 1. 이미 oauth_id + provider로 등록된 유저가 있는지 확인
        Optional<OauthAccount> existingOauth = oauthAccountRepository.findByProviderAndOauthId(provider, oauthId);
        User user;
        if (existingOauth.isPresent()) {
            // 기존 회원이면 해당 user 가져오기
            user = existingOauth.get().getUser();
        } else {
            // 2. 중복 이메일 확인
            if (userRepository.existsByEmail(email)) {
                throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
            }

            // 3. 신규 User 생성
            user = User.builder()
                    .nickname(nickname)
                    .email(email)
                    .password(null)
                    .uuid(UUID.randomUUID().toString())
                    .isEmailVerified(true)
                    .role(Role.SOCIAL)
                    .profileImageUrl(profileImageUrl)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);

            // 4. OauthAccount 저장
            OauthAccount oauthAccount = OauthAccount.builder()
                    .user(user)
                    .provider(provider)
                    .oauthId(oauthId)
                    .build();

            oauthAccountRepository.save(oauthAccount);
        }

        // 5. 토큰 발급
        String accessToken = tokenProvider.createAccessToken(user.getUuid());
        String refreshToken = tokenProvider.createRefreshToken(user.getUuid());

        // 6. 리프레시 토큰 저장
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // 7. DTO 반환
        return UserLoginResponseDto.entityToDto(user, accessToken, refreshToken, user.getRole().name());
    }


    @Override
    @Transactional
    public UserLoginResponseDto LoginUser(UserRequestDto userRequestDto) {

        // 400 : 데이터 누락
        if (userRequestDto.getEmail() == null || userRequestDto.getPassword() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 회원 존재 X 2106
        User user = userRepository.findByEmail(userRequestDto.getEmail());
        if (user == null || !bCryptPasswordEncoder.matches(userRequestDto.getPassword(),
            user.getPassword())) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        String accessToken = tokenProvider.createAccessToken(user.getUuid());
        String refreshToken = tokenProvider.createRefreshToken(user.getUuid());

        User updateUser = User.builder()
            .user_id(user.getUser_id())
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

        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.entityToDto(user,
            accessToken, refreshToken, user.getRole().name());

        return userLoginResponseDto;
    }

    @Override
    public ShowUserResponseDto ShowUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        ShowUserResponseDto showUserResponseDto = ShowUserResponseDto.entityToDto(user);
        return showUserResponseDto;
    }

    @Override
    @Transactional
    public void updateUser(String nickname, String email, MultipartFile profileImage) {
//        System.out.println("[디버깅] nickname: " + nickname);
//        System.out.println("[디버깅] email: " + email);
//        System.out.println("[디버깅] profileImage: " + (profileImage != null ? profileImage.getOriginalFilename() : "null"));
//        System.out.println("[서비스] profileImage.getSize(): " + (profileImage != null ? profileImage.getSize() : -1));

        if (nickname == null && email == null && (profileImage == null || profileImage.isEmpty())) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        String newProfileImageUrl = user.getProfileImageUrl();
        if (profileImage != null) {
            newProfileImageUrl = fileUploadService.upload(profileImage);
        }

        User updatedUser = User.builder()
                .user_id(user.getUser_id())
                .nickname(nickname != null ? nickname : user.getNickname())
                .email(email != null ? email : user.getEmail())
                .profileImageUrl(newProfileImageUrl)
                .password(user.getPassword())
                .uuid(user.getUuid())
                .role(user.getRole())
                .refreshToken(user.getRefreshToken())
                .build();

        userRepository.save(updatedUser);
    }



    @Override
    @Transactional
    public void deleteUser(UserRequestDto userRequestDto) {
        // 400 : 데이터 누락
        if (userRequestDto == null) {
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
        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MISMATCH);
        }
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void chgPassword(ChgPasswordRequestDto chgPasswordRequestDto) {
        // 400 : 데이터 누락
        if (chgPasswordRequestDto == null || chgPasswordRequestDto.getCurrentPassword() == null ||
            chgPasswordRequestDto.getNewPassword() == null ||
            chgPasswordRequestDto.getEmail() == null ||
            chgPasswordRequestDto.getConfirmPassword() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        // 사용자 조회
        User user = userRepository.findByEmail(chgPasswordRequestDto.getEmail());
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        // 현재 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(chgPasswordRequestDto.getCurrentPassword(),
            user.getPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_CURRENT_PASSWORD);
        }

        // 2601 : 비밀번호 형식 불일치
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        if (!(chgPasswordRequestDto.getNewPassword()).matches(regex)) {
            throw new BaseException(BaseResponseStatus.PASSWORD_FORMAT_INVALID);
        }

        // 2602 : 비밀번호 불일치
        if (!chgPasswordRequestDto.getNewPassword()
            .equals(chgPasswordRequestDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        user = User.builder()
            .user_id(user.getUser_id())
            .nickname(user.getNickname())
            .password(bCryptPasswordEncoder.encode(chgPasswordRequestDto.getNewPassword()))
            .email(user.getEmail())
            .uuid(user.getUuid())
            .role(user.getRole())
            .profileImageUrl(user.getProfileImageUrl())
            .refreshToken(user.getRefreshToken())
            .isEmailVerified(user.isEmailVerified())
            .build();

        userRepository.save(user);
    }

    public UserTokenResponseDto rotateToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        String newAccessToken = tokenProvider.createAccessToken(user.getUuid());
        String newRefreshToken = tokenProvider.createRefreshToken(user.getUuid());

        User updatedUser = User.builder()
            .user_id(user.getUser_id())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .password(user.getPassword())
            .uuid(user.getUuid())
            .role(user.getRole())
            .profileImageUrl(user.getProfileImageUrl())
            .refreshToken(newRefreshToken)
            .build();

        userRepository.save(updatedUser);

        UserTokenResponseDto userTokenResponseDto = UserTokenResponseDto.entityToDto(
            newAccessToken,
            newRefreshToken);
        return userTokenResponseDto;
    }
}
