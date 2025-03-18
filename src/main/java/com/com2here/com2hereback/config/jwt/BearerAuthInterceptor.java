package com.com2here.com2hereback.config.jwt;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.UserTokenResponseDto;
import com.com2here.com2hereback.repository.UserRepository;
import com.com2here.com2hereback.vo.UserTokenResponseVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// Bearer 토큰 인터셉터
@Component
public class BearerAuthInterceptor implements HandlerInterceptor {

    public static final String ACCESS_TOKEN_TYPE = "Bearer"; // Access Token 타입

    private final AuthorizationExtractor authExtractor;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, TokenProvider tokenProvider, UserRepository userRepository) {
        this.authExtractor = authExtractor;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        System.out.println(">>> interceptor.preHandle 호출");
        String accessToken = authExtractor.extract(request, ACCESS_TOKEN_TYPE).replaceAll("\\s+", "");
        String refreshToken = authExtractor.extractRefreshToken(request);
        // accessToken 유효시간 검증 성공 시
        if (tokenProvider.validateAccessToken(accessToken)) {
            String uuid = tokenProvider.getSubject(accessToken);

            Authentication authentication = new UsernamePasswordAuthenticationToken(uuid, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("uuid", uuid);
            return true;
        }else {
            BaseResponseStatus status;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.setContentType("application/json;charset=utf-8");

            // 2005 : 토큰이 만료되었습니다.
            if(tokenProvider.validateRefreshToken(refreshToken)) {
                String uuid = tokenProvider.getSubject(refreshToken);
                User user = userRepository.findByUuid(uuid);
                if(user.getRefreshToken().equals(refreshToken)) {
                    String newAccessToken = tokenProvider.createAccessToken(user.getUuid());
                    String newRefreshToken = tokenProvider.createRefreshToken(user.getUuid());

                    User updatedUser = User.builder()
                        .user_id(user.getUser_id())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .uuid(user.getUuid())
                        .refreshToken(newRefreshToken)
                        .build();

                    userRepository.save(updatedUser);

                    UserTokenResponseDto userTokenResponseDto = UserTokenResponseDto.entityToDto(newAccessToken, newRefreshToken);
                    UserTokenResponseVo userTokenResponseVo = UserTokenResponseVo.dtoToVo(
                        userTokenResponseDto);

                    status = BaseResponseStatus.ACCESS_TOKEN_RETURNED_SUCCESS;
                    CMResponse cmResponse = CMResponse.success(status.getCode(), status,
                        userTokenResponseVo);
                    writeResponse(response, cmResponse);
                    return false;
                }
            }
            status = BaseResponseStatus.TOKEN_EXPIRED;
            // Refresh Token이 유효하지 않거나 유저를 찾을 수 없는 경우
            CMResponse cmResponse = CMResponse.fail(status.getCode(), status);
            writeResponse(response, cmResponse);
            return false;
        }


    }

    private void writeResponse(HttpServletResponse response, CMResponse cmResponse) {
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(cmResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
