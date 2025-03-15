package com.com2here.com2hereback.service.oauthservice;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.com2here.com2hereback.client.OauthClient;
import com.com2here.com2hereback.dto.oauthinfo.GoogleInfo;
import com.com2here.com2hereback.dto.oauthtoken.GoogleToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final OauthClient client;

    @Value("${google.auth-url}")
    private String googleAuthUrl;

    @Value("${google.user-api-url}")
    private String googleUserApiUrl;

    @Value("${google.restapi-key}")
    private String restapiKey;

    @Value("${google.redirect-url}")
    private String redirectUrl;

    @Value("${google.scope}")
    private String googleScope;

    @Value("${google.client-secret}")
    private String clientSecret; // client_secret 추가

    // GoogleInfo를 받아오는 메서드
    public GoogleInfo getInfo(final String code) {
        final GoogleToken token = getToken(code);
        log.debug("Received token: {}", token);

        if (token != null && token.getAccessToken() != null) {
            try {
                GoogleInfo googleInfo = client.getGoogleInfo(new URI(googleUserApiUrl),
                        token.getTokenType() + " " + token.getAccessToken());
                log.debug("GoogleInfo: {}", googleInfo);
                return googleInfo;
            } catch (Exception e) {
                log.error("Error while requesting Google Info", e);
                return GoogleInfo.fail(); // API 요청 실패 처리
            }
        } else {
            log.error("Failed to get token or token is null.");
            return GoogleInfo.fail(); // 토큰이 없으면 실패 처리
        }
    }

    private GoogleToken getToken(final String code) {
        try {
            GoogleToken token = client.getGoogleToken(new URI(googleAuthUrl), restapiKey, redirectUrl, code,
                    "authorization_code", clientSecret, googleScope); // client_secret 전달
            log.debug("Google Token: {}", token);
            return token;
        } catch (Exception e) {
            log.error("Error while getting Google Token", e);
            return GoogleToken.fail(); // 토큰 요청 실패 처리
        }
    }
}
