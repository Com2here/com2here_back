package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.AdminUserRegisterReqDto;
import com.com2here.com2hereback.dto.AdminUserUpdateReqDto;
import com.com2here.com2hereback.dto.UserShowRespDto;
import com.com2here.com2hereback.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public void createUserByAdmin(AdminUserRegisterReqDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .nickname(registerDto.getNickname())
                .password(bCryptPasswordEncoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .role(registerDto.getRole())
                .profileImageUrl(registerDto.getProfileImageUrl())
                .uuid(null)
                .refreshToken(null)
                .createdAt(LocalDateTime.now())
                .lastLoginAt(null)
                .build();

        userRepository.save(user);
    }
    
    @Override
    public Page<UserShowRespDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                             .map(UserShowRespDto::entityToDto);
    }

    @Override
    @Transactional
    public void updateUserByAdmin(String uuid, AdminUserUpdateReqDto updateDto) {
        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        String updatedImageUrl = user.getProfileImageUrl();
        if (updateDto.getProfileImage() != null && !updateDto.getProfileImage().isEmpty()) {
            updatedImageUrl = fileStorageService.upload(updateDto.getProfileImage());
        }

        User updatedUser = User.builder()
                .userId(user.getUserId())
                .uuid(user.getUuid())
                .nickname(updateDto.getNickname() != null ? updateDto.getNickname() : user.getNickname())
                .email(updateDto.getEmail() != null ? updateDto.getEmail() : user.getEmail())
                .password(user.getPassword())
                .role(updateDto.getRole() != null ? updateDto.getRole() : user.getRole())
                .isEmailVerified(user.isEmailVerified())
                .profileImageUrl(updatedImageUrl)
                .refreshToken(user.getRefreshToken())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();

        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserByAdmin(String uuid) {
        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        userRepository.delete(user);
    }
}
