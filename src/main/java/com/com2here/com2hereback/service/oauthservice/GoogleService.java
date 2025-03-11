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

    // @Value("${google.client-id}")
    // private String clientId;

    // @Value("${google.client-secret}")
    // private String clientSecret;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    @Value("${google.redirect-url}")
    private String redirectUrl;

    @Value("${google.scope}")
    private String googleScope;

    public GoogleInfo getInfo(final String code) {
        final GoogleToken token = getToken(code);
        log.debug("token = {}", token);
        try {
            return client.getGoogleInfo(new URI(googleUserApiUrl), "Bearer " + token.getAccessToken());
        } catch (Exception e) {
            log.error("Something went wrong while fetching Google user info.", e);
            return GoogleInfo.fail();
        }
    }

    private GoogleToken getToken(final String code) {
        try {
            return client.getGoogleToken(new URI(googleAuthUrl), restapiKey, redirectUrl, code,
                    "authorization_code", googleScope);
        } catch (Exception e) {
            log.error("Something went wrong while fetching Google token.", e);
            return GoogleToken.fail();
        }
    }
}
