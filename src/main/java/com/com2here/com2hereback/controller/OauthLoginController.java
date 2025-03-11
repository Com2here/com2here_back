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
import com.com2here.com2hereback.dto.oauthinfo.GoogleInfo;
import com.com2here.com2hereback.dto.oauthinfo.KakaoInfo;
import com.com2here.com2hereback.dto.oauthinfo.NaverInfo;
import com.com2here.com2hereback.dto.oauthtoken.NaverToken;
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
    private String kakaoclientId;

    @Value("${kakao.redirectUrl}")
    private String kakaoredirectUri;

    @Value("${naver.restapi-key}")
    private String naverclientId;

    @Value("${naver.redirectUrl}")
    private String naverredirectUri;

    @Value("${google.restapi-key}")
    private String googleclientId;

    @Value("${google.redirectUrl}")
    private String googleredirectUri;

    /**
     * 카카오 로그인 URL 반환
     */
    @GetMapping("/login/kakao/url")
    public Map<String, String> getKakaoLoginUrl() throws UnsupportedEncodingException {

        // redirectUri에서 불필요한 부분 제거 (예시로 localhost:3000/callback만 사용)
        String cleanRedirectUri = kakaoredirectUri.replace(" ", "").replace("kakao", "");

        // URL 인코딩 처리
        String encodedRedirectUri = URLEncoder.encode(cleanRedirectUri, "UTF-8");

        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + kakaoclientId
                + "&redirect_uri=" + encodedRedirectUri;

        Map<String, String> response = new HashMap<>();
        response.put("url", url); // JSON 형식으로 반환

        return response;
    }

    @GetMapping("/callback/kakao")
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

    /**
     * 네이버 로그인 URL 반환
     */
    @GetMapping("/login/naver/url")
    public Map<String, String> getNaverLoginUrl() throws UnsupportedEncodingException {

        // redirectUri에서 불필요한 부분 제거 (예시로 localhost:3000/callback만 사용)
        String cleanRedirectUri = naverredirectUri.replace(" ", "").replace("naver", "");

        // URL 인코딩 처리
        String encodedRedirectUri = URLEncoder.encode(cleanRedirectUri, "UTF-8");

        String url = "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=" + naverclientId
                + "&redirect_uri=" + encodedRedirectUri;

        Map<String, String> response = new HashMap<>();
        response.put("url", url); // JSON 형식으로 반환

        return response;
    }

    @GetMapping("/callback/naver")
    public void getNaverAccount(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.debug("Naver code = {}", code);

        NaverInfo naverInfo = naverService.getInfo(code);
        NaverAccount naverAccount = naverInfo.getNaverAccount();

        if (naverAccount != null) {
            log.debug("Naver account found: {}", naverAccount);
            // 로그인 성공 후 리다이렉트 URL로 이동 (메인 페이지)
            response.sendRedirect("http://localhost:5173"); // 메인 페이지로 리다이렉트
        } else {
            log.error("Failed to retrieve Naver account.");
            // 실패 시 리다이렉트
            response.sendRedirect("http://localhost:5173/login"); // 로그인 실패 시 login 페이지로 이동
        }
    }

    /**
     * 구글 로그인 URL 반환
     */
    @GetMapping("/login/google/url")
    public Map<String, String> getGoogleLoginUrl() throws UnsupportedEncodingException {

        // redirectUri에서 불필요한 부분 제거 (예시로 localhost:3000/callback만 사용)
        String cleanRedirectUri = googleredirectUri.replace(" ", "").replace("google", "");

        // URL 인코딩 처리
        String encodedRedirectUri = URLEncoder.encode(cleanRedirectUri, "UTF-8");

        String url = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?response_type=code"
                + "&client_id=" + googleclientId
                + "&redirect_uri=" + encodedRedirectUri
                + "&scope=openid%20profile%20email"; // 구글 로그인에 필요한 scope 추가

        Map<String, String> response = new HashMap<>();
        response.put("url", url); // JSON 형식으로 반환

        return response;
    }

    @GetMapping("/callback/google")
    public void getGoogleAccount(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.debug("Google code = {}", code);

        GoogleInfo googleInfo = googleService.getInfo(code);
        GoogleAccount googleAccount = googleInfo.getGoogleAccount();

        if (googleAccount != null) {
            log.debug("Google account found: {}", googleAccount);
            // 로그인 성공 후 리다이렉트 URL로 이동 (메인 페이지)
            response.sendRedirect("http://localhost:5173"); // 메인 페이지로 리다이렉트
        } else {
            log.error("Failed to retrieve Google account.");
            // 실패 시 리다이렉트
            response.sendRedirect("http://localhost:5173/login"); // 로그인 실패 시 login 페이지로 이동
        }
    }

}
