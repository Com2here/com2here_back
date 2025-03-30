package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.OauthRequestDto;
import com.com2here.com2hereback.dto.OauthResponseDto;
import com.com2here.com2hereback.service.oauthservice.OauthService;
import com.com2here.com2hereback.vo.OauthResponseVo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class OauthController {
    private final OauthService oauthService;

    @Value("${naver.restapi-key}")
    private String naverclientId;

    @Value("${naver.redirectUrl}")
    private String naverredirectUri;

    @Value("${google.restapi-key}")
    private String googleclientId;

    @Value("${google.redirectUrl}")
    private String googleredirectUri;

    /*
    카카오 소셜 로그인 요청 api
    입력값 : 카카오 로그인 화면에 정보 입력
    출력값 : code, clientId, kakaoredirectUri을 포함한 url을 반환함
     */
    @GetMapping("/login/kakao/url")
    public ResponseEntity<?> getKakaoLoginUrl(){
        try{
            CMResponse status = oauthService.createKakaoOauthUrl();
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), status.getData()));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /*
    카카오 소셜 로그인 정보 요청 api
    입력값 : code
    출력값 : email, (profile),
     */
    @PostMapping("/callback/kakao")
    public ResponseEntity<?> getKakaoAccount(@RequestBody OauthRequestDto oauthRequestDto) {
        try {
            CMResponse status = oauthService.getKakaoUserInfo(oauthRequestDto.getCode());
            OauthResponseVo oauthResponseVo = OauthResponseVo.dtoToVo(
                (OauthResponseDto) status.getData());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), oauthResponseVo));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /*
    네이버 소셜 로그인 요청 api
    입력값 : 네이버 로그인 화면에 정보 입력
    출력값 : code, clientId, kakaoredirectUri을 포함한 url을 반환함
     */
    @GetMapping("/login/naver/url")
    public ResponseEntity<?> getNaverLoginUrl() {

        try{
            CMResponse status = oauthService.createNaverOauthUrl();
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), status.getData()));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /*
    네이버 소셜 로그인 정보 요청 api
    입력값 : code
    출력값 : accessToken, refreshToken, email, username
     */
    @PostMapping("/callback/naver")
    public ResponseEntity<?> getNaverAccount(@RequestBody OauthRequestDto oauthRequestDto) {
        try {
            CMResponse status = oauthService.getNaverUserInfo(oauthRequestDto.getCode());
            OauthResponseVo oauthResponseVo = OauthResponseVo.dtoToVo(
                (OauthResponseDto) status.getData());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), oauthResponseVo));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /*
    구글 소셜 로그인 요청 api
    입력값 : 구글 로그인 화면에 정보 입력
    출력값 : code, clientId, kakaoredirectUri을 포함한 url을 반환함
     */
    @GetMapping("/login/google/url")
    public ResponseEntity<?> getGoogleLoginUrl() {

        try{
            CMResponse status = oauthService.createGoogleOauthUrl();
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), status.getData()));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /*
    구글 소셜 로그인 정보 요청 api
    입력값 : code
    출력값 : accessToken, refreshToken, email, username
     */
    @PostMapping("/callback/google")
    public ResponseEntity<?> getGoogleAccount(@RequestBody OauthRequestDto oauthRequestDto) {
        try {
            CMResponse status = oauthService.getGoogleUserInfo(oauthRequestDto.getCode());
            OauthResponseVo oauthResponseVo = OauthResponseVo.dtoToVo(
                (OauthResponseDto) status.getData());
            return ResponseEntity.ok().body(new CMResponse<>(status.getCode(), status.getMessage(), oauthResponseVo));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new CMResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
        }
    }


}
