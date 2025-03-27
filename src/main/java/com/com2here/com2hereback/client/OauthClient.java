package com.com2here.com2hereback.client;

import java.net.URI;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.com2here.com2hereback.config.OauthFeignConfiguration;
import com.com2here.com2hereback.dto.oauthinfo.GoogleInfo;
import com.com2here.com2hereback.dto.oauthinfo.KakaoInfo;
import com.com2here.com2hereback.dto.oauthinfo.NaverInfo;
import com.com2here.com2hereback.dto.oauthtoken.GoogleToken;
import com.com2here.com2hereback.dto.oauthtoken.KakaoToken;
import com.com2here.com2hereback.dto.oauthtoken.NaverToken;

@FeignClient(name = "OauthClient", configuration = OauthFeignConfiguration.class)
@Component
public interface OauthClient {

        // Kakao
        @PostMapping
        KakaoInfo getKakaoInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping("/oauth/token") // 정확한 URL 경로를 명시
        KakaoToken getKakaoToken(URI basUri, @RequestParam("client_id") String restApiKey,
                        @RequestParam("redirect_uri") String redirectUrl,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType);

        // Google
        @PostMapping
        GoogleInfo getGoogleInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping
        GoogleToken getGoogleToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                        @RequestParam("client-secret") String clientsecret,
                        @RequestParam("redirect_uri") String redirectUri,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType,
                        @RequestParam("scope") String scope, // scope 추가
                        @RequestParam("access_type") String accessType); // access_type 추가

        // Naver
        @PostMapping
        NaverInfo getNaverInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping
        NaverToken getNaverToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                        @RequestParam("redirect_uri") String redirectUri,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType,
                        @RequestParam("client-secret") String clientsecret);
}
