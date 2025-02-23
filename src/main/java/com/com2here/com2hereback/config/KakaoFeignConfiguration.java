package com.com2here.com2hereback.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;

@Configuration
@EnableFeignClients(basePackages = "com.com2here.com2hereback.client") // Feign 클라이언트가 있는 패키지 경로
@ImportAutoConfiguration({ FeignAutoConfiguration.class, HttpClientConfiguration.class })
public class KakaoFeignConfiguration {
    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }
}
