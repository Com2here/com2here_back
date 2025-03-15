package com.com2here.com2hereback.service.oauthservice;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.com2here.com2hereback.client.OauthClient;
import com.com2here.com2hereback.dto.oauthinfo.NaverInfo;
import com.com2here.com2hereback.dto.oauthtoken.NaverToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

    private final OauthClient client;

    @Value("${naver.auth-url}")
    private String naverAuthUrl;

    @Value("${naver.user-api-url}")
    private String naverUserApiUrl;

    @Value("${naver.restapi-key}")
    private String restapiKey;

    // @Value("${naver.client-id}")
    // private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-url}")
    private String redirectUrl;

    public NaverInfo getInfo(final String code) {
        final NaverToken token = getToken(code);
        log.debug("Received token: {}", token);

        if (token != null && token.getAccessToken() != null) {
            try {
                NaverInfo naverInfo = client.getNaverInfo(new URI(naverUserApiUrl),
                        "Bearer " + token.getAccessToken());
                log.debug("NaverInfo: {}", naverInfo);
                return naverInfo;
            } catch (Exception e) {
                log.error("Error while requesting Naver Info", e);
                return NaverInfo.fail(); // API 요청 실패 처리
            }
        } else {
            log.error("Failed to get token or token is null.");
            return NaverInfo.fail(); // 토큰이 없으면 실패 처리
        }
    }

    private NaverToken getToken(final String code) {
        try {
            NaverToken token = client.getNaverToken(new URI(naverAuthUrl), restapiKey, redirectUrl, code,
                    "authorization_code", clientSecret);
            log.debug("Naver Token: {}", token);
            return token;
        } catch (Exception e) {
            log.error("Error while getting Naver Token", e);
            return NaverToken.fail(); // 토큰 요청 실패 처리
        }
    }
}
