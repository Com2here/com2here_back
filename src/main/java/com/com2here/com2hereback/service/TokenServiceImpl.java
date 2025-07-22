package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.config.jwt.TokenProvider;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.TokenRespDto;
import com.com2here.com2hereback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenServiceImpl implements TokenService{

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public TokenRespDto rotateToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        String newAccessToken = tokenProvider.createAccessToken(user.getUuid());
        String newRefreshToken = tokenProvider.createRefreshToken(user.getUuid());

        User updatedUser = User.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .password(user.getPassword())
            .uuid(user.getUuid())
            .role(user.getRole())
            .profileImageUrl(user.getProfileImageUrl())
            .refreshToken(newRefreshToken)
            .build();

        userRepository.save(updatedUser);

        TokenRespDto userTokenResponseDto = TokenRespDto.entityToDto(
            newAccessToken,
            newRefreshToken);
        return userTokenResponseDto;
    }
}
