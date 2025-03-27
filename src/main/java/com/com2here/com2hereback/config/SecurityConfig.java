package com.com2here.com2hereback.config;

import com.com2here.com2hereback.config.jwt.AuthorizationExtractor;
import com.com2here.com2hereback.config.jwt.JwtAuthenticationFilter;
import com.com2here.com2hereback.config.jwt.TokenProvider;
import com.com2here.com2hereback.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/email/send", "/api/v1/email/verify", "/api/v1/user/password/reset",
                                "/api/v1/user/register", "/api/v1/user/login", "/api/v1/user/login/kakao/url",
                                "/api/v1/user/login/naver/url", "/api/v1/user/login/google/url",
                                "/api/v1/user/callback/kakao")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정 추가

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // 원하는 출처를 추가
        configuration.addAllowedMethod("*"); // 허용할 HTTP 메서드를 지정
        configuration.addAllowedHeader("*"); // 허용할 헤더를 지정
        configuration.setExposedHeaders(List.of("Authorization")); // 응답에서 노출할 헤더 추가

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 설정을 적용
        return source;
    }
}
