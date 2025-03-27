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

    public KakaoInfo getInfo(final String code) {
        final KakaoToken token = getToken(code);
        log.debug("Received token: {}", token);

        if (token != null && token.getAccessToken() != null) {
            try {
                KakaoInfo kakaoInfo = client.getKakaoInfo(
                        new URI(kakaoUserApiUrl), // 여기서 baseUrl을 설정
                        token.getTokenType() + " " + token.getAccessToken());
                log.debug("KakaoInfo: {}", kakaoInfo);
                return kakaoInfo;
            } catch (Exception e) {
                log.error("Error while requesting Kakao Info", e);
                return KakaoInfo.fail(); // 실패 처리
            }
        } else {
            log.error("Failed to get token or token is null.");
            return KakaoInfo.fail(); // 실패 처리
        }
    }

    public KakaoToken getToken(final String code) {
        try {
            KakaoToken token = client.getKakaoToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code,
                    "authorization_code");
            log.debug("Kakao Token: {}", token);
            return token;
        } catch (Exception e) {
            log.error("Error while getting Kakao Token", e);
            return KakaoToken.fail();
        }
    }
}
