package com.com2here.com2hereback.config;

import com.com2here.com2hereback.config.jwt.AuthorizationExtractor;
import com.com2here.com2hereback.config.jwt.JwtAuthenticationFilter;
import com.com2here.com2hereback.config.jwt.TokenProvider;
import com.com2here.com2hereback.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthorizationExtractor authExtractor;

    public SecurityConfig(TokenProvider tokenProvider, UserRepository userRepository,
            AuthorizationExtractor authExtractor) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.authExtractor = authExtractor;
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider, userRepository, authExtractor);
    }

    @Bean // -> 암호화
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/email/send", "/api/v1/email/verify", "/api/v1/email/password/reset",
                                "/api/v1/user/register", "/api/v1/user/login", "api/v1/user/login/kakao/url",
                                "api/v1/user/login/naver/url", "api/v1/user/login/google/url", "/api/v1/email/authcode")
                        .permitAll()
                        // .requestMatchers("/**").permitAll() // 모든 경로에 대해 접근 허용
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
