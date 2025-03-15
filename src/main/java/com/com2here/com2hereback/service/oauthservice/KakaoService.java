package com.com2here.com2hereback.service.oauthservice;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.com2here.com2hereback.client.OauthClient;
import com.com2here.com2hereback.dto.oauthinfo.KakaoInfo;
import com.com2here.com2hereback.dto.oauthtoken.KakaoToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final OauthClient client;

    @Value("${kakao.auth-url}")
    private String kakaoAuthUrl;

    @Value("${kakao.user-api-url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    // KakaoInfo를 받아오는 메서드
    public KakaoInfo getInfo(final String code) {
        final KakaoToken token = getToken(code);
        log.debug("Received token: {}", token);

        if (token != null && token.getAccessToken() != null) {
            try {
                KakaoInfo kakaoInfo = client.getKakaoInfo(new URI(kakaoUserApiUrl),
                        token.getTokenType() + " " + token.getAccessToken());
                log.debug("KakaoInfo: {}", kakaoInfo);

                // refreshToken도 확인해서 로그로 남기기
                if (token.getRefreshToken() != null) {
                    log.debug("Kakao Refresh Token: {}", token.getRefreshToken());
                } else {
                    log.warn("Kakao Refresh Token is null.");
                }

                return kakaoInfo;
            } catch (Exception e) {
                log.error("Error while requesting Kakao Info", e);
                return KakaoInfo.fail(); // API 요청 실패 처리
            }
        } else {
            log.error("Failed to get token or token is null.");
            return KakaoInfo.fail(); // 토큰이 없으면 실패 처리
        }
    }

    private KakaoToken getToken(final String code) {
        try {
            KakaoToken token = client.getKakaoToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code,
                    "authorization_code");
            log.debug("Kakao Token: {}", token);

            // refreshToken도 확인해서 로그 남기기
            if (token.getRefreshToken() != null) {
                log.debug("Received Kakao Refresh Token: {}", token.getRefreshToken());
            } else {
                log.warn("No Kakao Refresh Token received.");
            }

            return token;
        } catch (Exception e) {
            log.error("Error while getting Kakao Token", e);
            return KakaoToken.fail(); // 토큰 요청 실패 처리
        }
    }
}
