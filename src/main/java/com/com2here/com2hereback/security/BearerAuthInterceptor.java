package com.com2here.com2hereback.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

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

        if (StringUtils.isEmpty(token)) {
            return true;
        }

        // Base64 URL 형식 확인
        if (!BASE64_URL_PATTERN.matcher(token).matches()) {
            throw new IllegalArgumentException("잘못된 토큰 형식");
        }

        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }
        int userId = tokenProvider.getSubject(token);
        request.setAttribute("userId", userId);
        return true;
    }
}
