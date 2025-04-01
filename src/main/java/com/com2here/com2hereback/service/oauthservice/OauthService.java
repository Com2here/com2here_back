package com.com2here.com2hereback.service.oauthservice;

import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.oauth.GoogleInfo;
import com.com2here.com2hereback.dto.oauth.GoogleToken;
import com.com2here.com2hereback.dto.oauth.KakaoInfo;
import com.com2here.com2hereback.dto.oauth.KakaoToken;
import com.com2here.com2hereback.dto.oauth.NaverInfo;
import com.com2here.com2hereback.dto.oauth.NaverToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;

public interface OauthService {
    CMResponse createGoogleOauthUrl();
    CMResponse getGoogleUserInfo(String code) throws URISyntaxException, JsonProcessingException;
    GoogleInfo getGoogleInfo(GoogleToken googleToken) throws URISyntaxException, JsonProcessingException;
    GoogleToken getGoogleToken(String code) throws URISyntaxException, JsonProcessingException;

    CMResponse createNaverOauthUrl();
    CMResponse getNaverUserInfo(String code) throws URISyntaxException, JsonProcessingException;
    NaverToken getNaverToken(String code)throws URISyntaxException, JsonProcessingException;
    NaverInfo getNaverInfo(NaverToken naverToken)throws URISyntaxException, JsonProcessingException;
    CMResponse createKakaoOauthUrl();
    CMResponse getKakaoUserInfo(String code) throws URISyntaxException, JsonProcessingException;
    KakaoInfo getKakaoInfo(KakaoToken kakaoToken) throws URISyntaxException, JsonProcessingException;
    KakaoToken getKakaoToken(String code) throws URISyntaxException, JsonProcessingException;
}
