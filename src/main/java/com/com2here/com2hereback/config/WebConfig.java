package com.com2here.com2hereback.config;

import com.com2here.com2hereback.security.BearerAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class
WebConfig implements WebMvcConfigurer {
    private final BearerAuthInterceptor bearerAuthInterceptor;

    public WebConfig(BearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println(">>> 인터셉터 등록");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/api/v1/user/show");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/api/v1/user/delete");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/api/v1/user/update");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정
            .allowedOrigins("*") // 모든 도메인 허용 (운영 환경에서는 특정 도메인으로 제한하는 것이 좋음)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // 허용할 HTTP 메서드
    }
}
