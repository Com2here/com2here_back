package com.com2here.com2hereback.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.com2here.com2hereback.client.KakaoClient;
import com.com2here.com2hereback.dto.NaverInfo;
import com.com2here.com2hereback.dto.NaverToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

    private final KakaoClient client;

    @Value("${naver.auth-url}")
    private String naverAuthUrl;

    @Value("${naver.user-api-url}")
    private String naverUserApiUrl;

    // @Value("${naver.client-id}")
    // private String clientId;

    // @Value("${naver.client-secret}")
    // private String clientSecret;

    @Value("${naver.redirect-url}")
    private String redirectUrl;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    public NaverInfo getInfo(final String code) {
        final NaverToken token = getToken(code);
        log.debug("token = {}", token);
        try {
            return client.getNaverInfo(new URI(naverUserApiUrl), "Bearer " + token.getAccessToken());
        } catch (Exception e) {
            log.error("Something went wrong while fetching Naver user info.", e);
            return NaverInfo.fail();
        }
    }

    private NaverToken getToken(final String code) {
        try {
            return client.getNaverToken(new URI(naverAuthUrl), restapiKey, redirectUrl, code, "authorization_code");
        } catch (Exception e) {
            log.error("Something went wrong while fetching Naver token.", e);
            return NaverToken.fail();
        }
    }
}
