package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.dto.OauthRequestDto;
import com.com2here.com2hereback.dto.OauthResponseDto;
import com.com2here.com2hereback.dto.SocialLoginRequestDto;
import com.com2here.com2hereback.dto.UserLoginResponseDto;
import com.com2here.com2hereback.service.OauthService;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.OauthResponseVo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OauthController {
    private final OauthService oauthService;
    private final UserService userService;

    @GetMapping("/{provider}")
    public CMResponse<String> getOauthLoginUrl(@PathVariable String provider) {
        try {
            String url;
            switch (provider.toLowerCase()) {
                case "kakao":
                    url = oauthService.createKakaoOauthUrl();
                    break;
                case "google":
                    url = oauthService.createGoogleOauthUrl();
                    break;
                case "naver":
                    url = oauthService.createNaverOauthUrl();
                    break;
                default:
                    throw new BaseException(BaseResponseStatus.INVALID_PROVIDER);
            }
            return CMResponse.success(BaseResponseStatus.SUCCESS, url);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{provider}")
    public CMResponse<OauthResponseVo> getOauthAccount(@PathVariable String provider,
            @RequestBody OauthRequestDto oauthRequestDto) {
        try {
            OauthResponseDto oauthResponsedto;
            switch (provider.toLowerCase()) {
                case "kakao":
                    oauthResponsedto = oauthService.getKakaoUserInfo(oauthRequestDto.getCode());
                    break;
                case "google":
                    oauthResponsedto = oauthService.getGoogleUserInfo(oauthRequestDto.getCode());
                    break;
                case "naver":
                    oauthResponsedto = oauthService.getNaverUserInfo(oauthRequestDto.getCode());
                    break;
                default:
                    throw new BaseException(BaseResponseStatus.INVALID_PROVIDER);
            }
            OauthResponseVo oauthResponseVo = OauthResponseVo.dtoToVo(oauthResponsedto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, oauthResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        } catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/social-login")
    public CMResponse<UserLoginResponseDto> socialLogin(@RequestBody SocialLoginRequestDto requestDto) {
        try {
            UserLoginResponseDto responseDto = userService.registerOrLoginSocialUser(
                    requestDto.getEmail(),
                    requestDto.getNickname(),
                    requestDto.getProvider(),
                    requestDto.getOauthId(),
                    requestDto.getProfileImageUrl()
            );
            return CMResponse.success(BaseResponseStatus.SUCCESS, responseDto);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        } catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
