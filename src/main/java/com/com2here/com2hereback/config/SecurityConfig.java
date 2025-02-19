package com.com2here.com2hereback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/signin", "/auth/signup").permitAll() // 로그인, 회원가입은 누구나 접근 가능
                        .anyRequest().authenticated()) // 나머지 요청은 인증 필요
                .formLogin(); // 기본 로그인 페이지 사용

        return http.build();
    }
}
