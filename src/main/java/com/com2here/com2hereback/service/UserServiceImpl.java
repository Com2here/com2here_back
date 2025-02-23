package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.ShowUserResponseDto;
import com.com2here.com2hereback.dto.UserRequestDto;
import com.com2here.com2hereback.dto.UserResponseDto;
import com.com2here.com2hereback.repository.UserRepository;
import com.com2here.com2hereback.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        System.out.println(userRequestDto.getPassword());
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
        User user = userRepository.findById(tokenProvider.getSubject(request.getHeader("Authorization")));
        ShowUserResponseDto showUserResponseDto = ShowUserResponseDto.entityToDto(user);
        return showUserResponseDto;
    }

    @Override
    @Transactional
    public void updateUser(UserRequestDto userRequestDto, HttpServletRequest request) {
        User user = userRepository.findById(tokenProvider.getSubject(request.getHeader("Authorization")));

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
    public int deleteUser(UserRequestDto userRequestDto, HttpServletRequest request) {

        User user = userRepository.findById(tokenProvider.getSubject(request.getHeader("Authorization")));

        // 입력된 비밀번호와 저장된 비밀번호 비교
        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            return 403;
        }

        // 비밀번호가 일치하면 사용자 삭제
        userRepository.delete(user);
        return 200;
    }


}
