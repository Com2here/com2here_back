package com.com2here.com2hereback.client;

import com.com2here.com2hereback.dto.oauth.KakaoInfo;
import com.com2here.com2hereback.dto.oauth.KakaoToken;
import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.com2here.com2hereback.config.OauthFeignConfiguration;

@FeignClient(name = "OauthClient", configuration = OauthFeignConfiguration.class)
@Component
public interface OauthClient {

        @PostMapping("/oauth/info")
        KakaoInfo getKakaoInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping("/oauth")
        KakaoToken getKakaoToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String restApiKey,
            @RequestParam("redirect_uri") String redirectUrl,
            @RequestParam("code") String code
        );

        // Google
        @PostMapping
        KakaoInfo getGoogleInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping
        KakaoToken getGoogleToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                        @RequestParam("client-secret") String clientsecret,
                        @RequestParam("redirect_uri") String redirectUri,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType,
                        @RequestParam("scope") String scope, // scope 추가
                        @RequestParam("access_type") String accessType); // access_type 추가

        // Naver
        @PostMapping
        KakaoInfo getNaverInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

        @PostMapping
        KakaoToken getNaverToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                        @RequestParam("redirect_uri") String redirectUri,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType,
                        @RequestParam("client-secret") String clientsecret);
}
