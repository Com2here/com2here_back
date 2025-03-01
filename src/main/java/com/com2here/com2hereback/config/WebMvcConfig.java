package com.com2here.com2hereback.config;

import com.com2here.com2hereback.security.BearerAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BearerAuthInterceptor bearerAuthInterceptor;

    public WebMvcConfig(BearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println(">>> 인터셉터 등록");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/api/v1/user/show");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/api/v1/user/delete");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/api/v1/user/update");
    }
}
