package com.com2here.com2hereback.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.com2here.com2hereback.dto.oauthaccount.GoogleAccount;
import com.com2here.com2hereback.dto.oauthaccount.KakaoAccount;
import com.com2here.com2hereback.dto.oauthaccount.NaverAccount;
import com.com2here.com2hereback.service.oauthservice.GoogleService;
import com.com2here.com2hereback.service.oauthservice.KakaoService;
import com.com2here.com2hereback.service.oauthservice.NaverService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OauthLoginController {

    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final NaverService naverService;

    /**
     * 카카오 로그인 콜백
     */
    @GetMapping("/callback/kakao")
    public KakaoAccount getKakaoAccount(@RequestParam("code") String code) {
        log.debug("Kakao code = {}", code);
        return kakaoService.getInfo(code).getKakaoAccount();
    }

    /**
     * 구글 로그인 콜백
     */
    @GetMapping("/callback/google")
    public GoogleAccount getGoogleAccount(@RequestParam("code") String code) {
        log.debug("Google code = {}", code);
        return googleService.getInfo(code).getGoogleAccount();
    }

    /**
     * 네이버 로그인 콜백
     */
    @GetMapping("/callback/naver")
    public NaverAccount getNaverAccount(@RequestParam("code") String code) {
        log.debug("Naver code = {}", code);
        return naverService.getInfo(code).getNaverAccount();
    }
}
