package com.com2here.com2hereback.config.jwt;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final AuthorizationExtractor authExtractor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info(">>> JwtAuthenticationFilter 호출: {}", request.getRequestURI());

        String path = request.getRequestURI();

        if (shouldSkipFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = path.equals("/api/v1/token/rotate")
                ? authExtractor.extractRefreshToken(request)
                : authExtractor.extract(request, "Bearer").replaceAll("\\s+", "");

        String tokenType = path.equals("/api/v1/token/rotate") ? "refresh" : "access";

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token, tokenType)) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(tokenProvider.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String uuid = claims.getSubject();
            String role = claims.get("role", String.class).toUpperCase();

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            System.out.println("권한 확인: " + authorities);

            Authentication authentication = new UsernamePasswordAuthenticationToken(uuid, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("uuid", uuid);
            filterChain.doFilter(request, response);
        } else {
            BaseException.sendErrorResponse(response, BaseResponseStatus.TOKEN_EXPIRED);
        }
    }

    private boolean shouldSkipFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/uploads/") ||
                path.startsWith("/api/v1/oauth/") ||
                path.startsWith("/api/v1/email/") ||
                path.equals("/api/v1/user/login") ||
                path.equals("/api/v1/user/register") ||
                path.equals("/api/v1/recommend");
    }
}
