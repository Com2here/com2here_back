package com.com2here.com2hereback.config;

import com.com2here.com2hereback.config.jwt.AuthorizationExtractor;
import com.com2here.com2hereback.config.jwt.JwtAuthenticationFilter;
import com.com2here.com2hereback.config.jwt.TokenProvider;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize("hasRole('ADMIN')") 사용 가능하도록 설정
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TokenProvider tokenProvider;
    private final AuthorizationExtractor authExtractor;

    public SecurityConfig(TokenProvider tokenProvider,
                          AuthorizationExtractor authExtractor) {
        this.tokenProvider = tokenProvider;
        this.authExtractor = authExtractor;
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider, authExtractor);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/uploads/**",
                                "/api/v1/email/**",
                                "/api/v1/oauth/*",
                                "/api/v1/user/register",
                                "/api/v1/user/login"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // 프론트엔드 주소
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
