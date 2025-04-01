package com.com2here.com2hereback.service.oauthservice;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.OauthResponseDto;
import com.com2here.com2hereback.dto.oauth.GoogleInfo;
import com.com2here.com2hereback.dto.oauth.GoogleToken;
import com.com2here.com2hereback.dto.oauth.KakaoInfo;
import com.com2here.com2hereback.dto.oauth.KakaoToken;
import com.com2here.com2hereback.dto.oauth.NaverInfo;
import com.com2here.com2hereback.dto.oauth.NaverToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OauthServiceImpl implements OauthService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate;

    @Value("${naver.authUrl}")
    private String naverAuthUrl;

    @Value("${naver.tokenUrl}")
    private String naverTokenUrl;

    @Value("${naver.userApiUrl}")
    private String naverUserApiUrl;

    @Value("${naver.restapiKey}")
    private String naverRestapiKey;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirectUrl}")
    private String naverRedirectUri;

    @Value("${kakao.authUrl}")
    private String kakaoAuthUrl;

    @Value("${kakao.tokenUrl}")
    private String kakaoTokenUrl;

    @Value("${kakao.userApiUrl}")
    private String kakaoUserApiUrl;

    @Value("${kakao.restapiKey}")
    private String kakaoRestapiKey;

    @Value("${kakao.redirectUrl}")
    private String kakaorRedirectUri;

    @Value("${google.authUrl}")
    private String googleAuthUrl;

    @Value("${google.userApiUrl}")
    private String googleUserApiUrl;

    @Value("${google.tokenUrl}")
    private String googleTokenUrl;

    @Value("${google.restapiKey}")
    private String googleRestapiKey;

    @Value("${google.redirectUrl}")
    private String googleRedirectUrl;

    @Value("${google.scope}")
    private String googleScope;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Override
    public CMResponse createGoogleOauthUrl() {
        BaseResponseStatus status;
        try{
            String url = googleAuthUrl
                + "?scope=email%20profile"
                + "&access_type=offline"
                + "&include_granted_scopes=true"
                + "&response_type=code"
                + "&redirect_uri=" + googleRedirectUrl
                + "&client_id=" + googleRestapiKey;


            status = BaseResponseStatus.SUCCESS;

            return CMResponse.success(status.getCode(), status, url);
        }catch (Exception e){
            status = BaseResponseStatus.FAIL_CREATE_OAUTH_URL;
            return CMResponse.fail(status.getCode(), status, null);
        }

    }

    @Override
    public CMResponse getGoogleUserInfo(String code)
        throws URISyntaxException, JsonProcessingException {
        BaseResponseStatus status;
        GoogleToken googleToken = getGoogleToken(code);
        GoogleInfo googleInfo = getGoogleInfo(googleToken);
        if(googleToken == null){
            status = BaseResponseStatus.FAIL_RETURN_OAUTH_TOKEN;
            return CMResponse.fail(status.getCode(), status, null);
        }

        OauthResponseDto oauthResponseDto = OauthResponseDto.entityToDto(googleInfo, googleToken);
        status = BaseResponseStatus.SUCCESS;

        return CMResponse.success(status.getCode(), status, oauthResponseDto);
    }

    @Override
    public GoogleToken getGoogleToken(String code)
        throws URISyntaxException, JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);

        String body = "grant_type=authorization_code"
            + "&client_id=" + googleRestapiKey
            + "&redirect_uri=" + googleRedirectUrl
            + "&code=" + decodedCode
            + "&client_secret=" + googleClientSecret;

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            new URI(googleTokenUrl),
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        Map<String, Object> jsonResponse = objectMapper.readValue(
            response.getBody(),
            new TypeReference<Map<String, Object>>() {}
        );
        GoogleToken googleToken = GoogleToken.entityToDto(jsonResponse);
        return googleToken;
    }


    @Override
    public GoogleInfo getGoogleInfo(GoogleToken googleToken)
        throws URISyntaxException, JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + googleToken.getAccessToken());
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            new URI(googleUserApiUrl),
            HttpMethod.GET,
            requestEntity,
            String.class
        );

        Map<String, Object> jsonResponse = objectMapper.readValue(
            response.getBody(),
            new TypeReference<Map<String, Object>>() {}
        );

        GoogleInfo googleInfo = GoogleInfo.entityToDto(jsonResponse);

        return googleInfo;
    }


    // 카카오 로그인 주소 생성 메소드
    @Override
    public CMResponse createKakaoOauthUrl() {
        BaseResponseStatus status;
        try{
            String url = kakaoAuthUrl
                + "?response_type=code"
                + "&client_id=" + kakaoRestapiKey
                + "&redirect_uri=" + kakaorRedirectUri;

            status = BaseResponseStatus.SUCCESS;

            return CMResponse.success(status.getCode(), status, url);
        }catch (Exception e){
            status = BaseResponseStatus.FAIL_CREATE_OAUTH_URL;
            return CMResponse.fail(status.getCode(), status, null);
        }

    }

    @Override
    public CMResponse getKakaoUserInfo(String code)
        throws URISyntaxException, JsonProcessingException {
        BaseResponseStatus status;
        KakaoToken kakaoToken = getKakaoToken(code);
        KakaoInfo kakaoInfo = getKakaoInfo(kakaoToken);
        if(kakaoToken == null){
            status = BaseResponseStatus.FAIL_RETURN_OAUTH_TOKEN;
            return CMResponse.fail(status.getCode(), status, null);
        }

        OauthResponseDto oauthResponseDto = OauthResponseDto.entityToDto(kakaoInfo, kakaoToken);
        status = BaseResponseStatus.SUCCESS;

        return CMResponse.success(status.getCode(), status, oauthResponseDto);
    }

    @Override
    public KakaoToken getKakaoToken(String code)
        throws URISyntaxException, JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=authorization_code"
            + "&client_id=" + kakaoRestapiKey
            + "&redirect_uri=" + kakaorRedirectUri
            + "&code=" + code;

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            new URI(kakaoTokenUrl),
            HttpMethod.POST,
            requestEntity,
            String.class
        );
        Map<String, Object> jsonResponse = objectMapper.readValue(
            response.getBody(),
            new TypeReference<Map<String, Object>>() {}
        );

        KakaoToken kakaoToken = KakaoToken.entityToDto(
            (String) jsonResponse.get("access_token"),
            (String) jsonResponse.get("refresh_token"),
            (String) jsonResponse.get("token_type"),
            ((Number) jsonResponse.get("expires_in")).longValue(),
            (String) jsonResponse.get("id_token"),
            ((Number) jsonResponse.get("refresh_token_expires_in")).longValue(),
            (String) jsonResponse.get("scope")
        );

        return kakaoToken;
    }

    @Override
    public KakaoInfo getKakaoInfo(KakaoToken kakaoToken)
        throws URISyntaxException, JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoToken.getAccessToken());
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            new URI(kakaoUserApiUrl),
            HttpMethod.GET,
            requestEntity,
            String.class
        );

        Map<String, Object> jsonResponse = objectMapper.readValue(
            response.getBody(),
            new TypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> kakaoAccount = (Map<String, Object>) jsonResponse.get("kakao_account");

        KakaoInfo kakaoInfo = KakaoInfo.entityToDto(jsonResponse, kakaoAccount);

        return kakaoInfo;
    }
    @Override
    public CMResponse createNaverOauthUrl() {
        BaseResponseStatus status;
        try{
            String url = naverAuthUrl
                + "?response_type=code"
                // + "&state=STATE_STRING"
                + "&client_id=" + naverRestapiKey
                + "&redirect_uri=" + naverRedirectUri;

            status = BaseResponseStatus.SUCCESS;

            return CMResponse.success(status.getCode(), status, url);
        }catch (Exception e){
            status = BaseResponseStatus.FAIL_CREATE_OAUTH_URL;
            return CMResponse.fail(status.getCode(), status, null);
        }

    }

    @Override
    public CMResponse getNaverUserInfo(String code)
        throws URISyntaxException, JsonProcessingException {
        BaseResponseStatus status;
        NaverToken naverToken = getNaverToken(code);
        NaverInfo naverInfo = getNaverInfo(naverToken);
        if(naverToken == null){
            status = BaseResponseStatus.FAIL_RETURN_OAUTH_TOKEN;
            return CMResponse.fail(status.getCode(), status, null);
        }

        OauthResponseDto oauthResponseDto = OauthResponseDto.entityToDto(naverInfo, naverToken);
        status = BaseResponseStatus.SUCCESS;

        return CMResponse.success(status.getCode(), status, oauthResponseDto);
    }

    @Override
    public NaverToken getNaverToken(String code)
        throws URISyntaxException, JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=authorization_code"
            + "&client_id=" + naverRestapiKey
            + "&redirect_uri=" + naverRedirectUri
            + "&code=" + code
            + "&client_secret=" + clientSecret;

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            new URI(naverTokenUrl),
            HttpMethod.POST,
            requestEntity,
            String.class
        );
        Map<String, Object> jsonResponse = objectMapper.readValue(
            response.getBody(),
            new TypeReference<Map<String, Object>>() {}
        );

        NaverToken naverToken = NaverToken.entityToDto(jsonResponse);
        return naverToken;
    }

    @Override
    public NaverInfo getNaverInfo(NaverToken naverToken)
        throws URISyntaxException, JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + naverToken.getAccessToken());
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            new URI(naverUserApiUrl),
            HttpMethod.GET,
            requestEntity,
            String.class
        );

        Map<String, Object> jsonResponse = objectMapper.readValue(
            response.getBody(),
            new TypeReference<Map<String, Object>>() {}
        );
        Map<String, Object> NaverAccount = (Map<String, Object>) jsonResponse.get("response");

        NaverInfo naverInfo = NaverInfo.entityToDto(NaverAccount);
        return naverInfo;
    }
}
