package com.com2here.com2hereback.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.oauthaccount.GoogleAccount;
import com.com2here.com2hereback.dto.oauthaccount.KakaoAccount;
import com.com2here.com2hereback.dto.oauthaccount.NaverAccount;
import com.com2here.com2hereback.dto.oauthinfo.KakaoInfo;
import com.com2here.com2hereback.service.oauthservice.GoogleService;
import com.com2here.com2hereback.service.oauthservice.KakaoService;
import com.com2here.com2hereback.service.oauthservice.NaverService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class OauthLoginController {

    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final NaverService naverService;

    @Value("${kakao.restapi-key}")
    private String clientId;

    @Value("${kakao.redirectUrl}")
    private String redirectUri;

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

    /**
     * 카카오 로그인 URL 반환
     */
    @GetMapping("/login/kakao/url")
    public Map<String, String> getKakaoLoginUrl() throws UnsupportedEncodingException {

        // redirectUri에서 불필요한 부분 제거 (예시로 localhost:3000/callback만 사용)
        String cleanRedirectUri = redirectUri.replace(" ", "").replace("kakao", "");

        // URL 인코딩 처리
        String encodedRedirectUri = URLEncoder.encode(cleanRedirectUri, "UTF-8");

        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + encodedRedirectUri;

        Map<String, String> response = new HashMap<>();
        response.put("url", url); // JSON 형식으로 반환

        return response;
    }

    @GetMapping("/callback")
    public void getKakaoAccount(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.debug("Kakao code = {}", code);

        KakaoInfo kakaoInfo = kakaoService.getInfo(code);
        KakaoAccount kakaoAccount = kakaoInfo.getKakaoAccount();

        if (kakaoAccount != null) {
            log.debug("Kakao account found: {}", kakaoAccount);
            // 로그인 성공 후 리다이렉트 URL로 이동 (메인 페이지)
            response.sendRedirect("http://localhost:5173"); // 메인 페이지로 리다이렉트
        } else {
            log.error("Failed to retrieve Kakao account.");
            // 실패 시 리다이렉트
            response.sendRedirect("http://localhost:5173/login"); // 로그인 실패 시 login 페이지로 이동
        }
    }

}
