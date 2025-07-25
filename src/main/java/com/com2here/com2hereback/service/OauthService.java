package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.OauthRespDto;
import com.com2here.com2hereback.dto.oauth.GoogleInfo;
import com.com2here.com2hereback.dto.oauth.GoogleToken;
import com.com2here.com2hereback.dto.oauth.KakaoInfo;
import com.com2here.com2hereback.dto.oauth.KakaoToken;
import com.com2here.com2hereback.dto.oauth.NaverInfo;
import com.com2here.com2hereback.dto.oauth.NaverToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;

public interface OauthService {
    String createGoogleOauthUrl();
    OauthRespDto getGoogleUserInfo(String code);
    GoogleInfo getGoogleInfo(GoogleToken googleToken) throws URISyntaxException, JsonProcessingException;
    GoogleToken getGoogleToken(String code) throws URISyntaxException, JsonProcessingException;

    String createNaverOauthUrl();
    OauthRespDto getNaverUserInfo(String code);
    NaverToken getNaverToken(String code)throws URISyntaxException, JsonProcessingException;
    NaverInfo getNaverInfo(NaverToken naverToken)throws URISyntaxException, JsonProcessingException;
    
    String createKakaoOauthUrl();
    OauthRespDto getKakaoUserInfo(String code);
    KakaoInfo getKakaoInfo(KakaoToken kakaoToken) throws URISyntaxException, JsonProcessingException;
    KakaoToken getKakaoToken(String code) throws URISyntaxException, JsonProcessingException;
}
