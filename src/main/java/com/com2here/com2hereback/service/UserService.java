package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.UserEntity;
import com.com2here.com2hereback.dto.UserDTO;
import com.com2here.com2hereback.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 비밀번호 암호화 및 중복 이메일 체크 후 회원가입 처리
    public UserEntity create(UserEntity user) {
        return userRepository.save(user);
    }

    // 이메일로 유저 정보 찾기 및 비밀번호 검증
    public UserEntity getByCredentials(String email, String password, PasswordEncoder passwordEncoder) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // 이메일 중복 체크
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
