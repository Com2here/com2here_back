package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserResponseDto;
import com.com2here.com2hereback.repository.UserRepository;
import com.com2here.com2hereback.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
        JwtTokenProvider jwtTokenProvider, JwtProvider jwtProvider) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProvider = jwtProvider;
    }

    // Access Token 반환
    @Override
    @Transactional
    public String RegisterUser(UserRequestDto userRequestDto) {
        // Dto 유무, 비밀번호 유무, 이메일 유무 확인
        System.out.println(userRequestDto.getPassword());
        System.out.println(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
        if (userRequestDto == null || userRequestDto.getPassword() == null || userRequestDto.getEmail() == null) {
            return "이메일과 비밀번호를 입력하세요.";
        }

        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        if (!(userRequestDto.getPassword()).matches(regex)) {
            return "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.";
        }

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            return "이미 등록된 이메일입니다.";
        }
        System.out.println(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
        User user = User.builder()
            .username(userRequestDto.getUsername())
            .password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
            .email(userRequestDto.getEmail()).build();

        System.out.println(userRequestDto.getPassword());
        userRepository.save(user);

        return "회원가입 성공";
    }

    @Override
    public UserResponseDto LoginUser(UserRequestDto userRequestDto) {
        String message;
        if (userRequestDto.getEmail() == null || userRequestDto.getPassword() == null) {
            message = "이메일과 비밀번호를 입력하세요.";
            UserResponseDto userResponseDto = UserResponseDto.builder()
                .message(message)
                .token(null)
                .build();
            return userResponseDto;
        }

        // 사용자 정보 찾기 및 비밀번호 검증
        User user = userRepository.findByEmail(userRequestDto.getEmail());
        if (user != null && bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            // 로그인 성공 시 JWT 토큰 발급 로직 추가
            // String token = tokenProvider.create(user); // 토큰 생성
            String token = jwtProvider.create(user);
            return UserResponseDto.builder()
                .message("로그인 성공")
                .token(token)
                .build();
        }

        message = "이메일 또는 비밀번호가 잘못되었습니다.";
        return UserResponseDto.builder()
            .message(message)
            .token(null)
            .build();
    }
}
