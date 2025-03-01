package com.com2here.com2hereback.security;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

// Bearer 토큰 인터셉터
@Component
public class BearerAuthInterceptor implements HandlerInterceptor {

    private static final Pattern BASE64_URL_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+){2}$");

    private AuthorizationExtractor authExtractor;
    private TokenProvider tokenProvider;

    public BearerAuthInterceptor(AuthorizationExtractor authExtractor, TokenProvider tokenProvider) {
        this.authExtractor = authExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        System.out.println(">>> interceptor.preHandle 호출");
        String token = authExtractor.extract(request, "Bearer").replaceAll("\\s+", "");
        if (tokenProvider.validateToken(token)) {
            int userId = tokenProvider.getSubject(token);

            // 사용자 정보를 기반으로 Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

            // SecurityContext에 Authentication 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 요청 속성에 userId 설정
            request.setAttribute("userId", userId);

        } else if (!BASE64_URL_PATTERN.matcher(token).matches()) {
            // 토큰 유효 X 2002
            throw new CustomException(BaseResponseStatus.TOKEN_NOT_VALID);
        } else {
            // 토큰 검증 실패 2005
            throw new CustomException(BaseResponseStatus.JWT_VALID_FAILED);
        }

        return true;
    }
}
