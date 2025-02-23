package com.com2here.com2hereback.client;

import java.net.URI;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.com2here.com2hereback.config.KakaoFeignConfiguration;
import com.com2here.com2hereback.dto.KakaoInfo;
import com.com2here.com2hereback.dto.KakaoToken;
import com.com2here.com2hereback.dto.GoogleInfo;
import com.com2here.com2hereback.dto.GoogleToken;
import com.com2here.com2hereback.dto.NaverInfo;
import com.com2here.com2hereback.dto.NaverToken;

@FeignClient(name = "kakaoClient", configuration = KakaoFeignConfiguration.class)
@Component
public interface KakaoClient {

        // Kakao
        @PostMapping
        KakaoInfo getKakaoInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping
        KakaoToken getKakaoToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                        @RequestParam("redirect_uri") String redirectUrl,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType);

        // Google
        @PostMapping
        GoogleInfo getGoogleInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping
        GoogleToken getGoogleToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                        // @RequestParam("client-secret") String clientsecret,
                        @RequestParam("redirect_uri") String redirectUri,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType,
                        @RequestParam("scope") String scope); // scope 추가

        // Naver
        @PostMapping
        NaverInfo getNaverInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping
        NaverToken getNaverToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                        @RequestParam("redirect_uri") String redirectUri,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType);
}
