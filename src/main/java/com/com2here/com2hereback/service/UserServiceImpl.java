package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.CMRespDto;
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
    public int RegisterUser(UserRequestDto userRequestDto) {

        User user = User.builder()
            .username(userRequestDto.getUsername())
            .password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
            .email(userRequestDto.getEmail()).build();

        userRepository.save(user);

        return 201;
    }

    @Override
    public UserResponseDto LoginUser(UserRequestDto userRequestDto) {

        User user = userRepository.findByEmail(userRequestDto.getEmail());
        if (user != null && bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            String token = tokenProvider.create(user);
            return UserResponseDto.builder()
                .message("로그인 성공")
                .token(token)
                .build();
        }
        return null;
    }

    @Override
    public ShowUserResponseDto ShowUser(HttpServletRequest request) {
        // SecurityContext에서 Authentication 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = (int) authentication.getPrincipal(); // userId를 가져옵니다.

        User user = userRepository.findById(userId);
        ShowUserResponseDto showUserResponseDto = ShowUserResponseDto.entityToDto(user);
        return showUserResponseDto;
    }

    @Override
    @Transactional
    public void updateUser(UserRequestDto userRequestDto, HttpServletRequest request) {
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
    }

    @Override
    @Transactional
    public BaseResponseStatus deleteUser(UserRequestDto userRequestDto, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = (int) authentication.getPrincipal(); // userId를 가져옵니다.

        // 2106
        User user = userRepository.findById(userId);
        if (user == null) {
            return BaseResponseStatus.NO_EXIST_MEMBERS; // 존재하지 않는 사용자
        }
        // 2202
        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            return BaseResponseStatus.PASSWORD_MISMATCH;
        }

        userRepository.delete(user);
        // 200
        return BaseResponseStatus.MEMBER_DELETE_SUCCESS;
    }


}
