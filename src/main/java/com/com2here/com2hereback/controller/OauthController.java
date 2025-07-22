package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.dto.OauthReqDto;
import com.com2here.com2hereback.dto.OauthRespDto;
import com.com2here.com2hereback.dto.SocialLoginReqDto;
import com.com2here.com2hereback.dto.UserLoginRespDto;
import com.com2here.com2hereback.service.OauthService;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.OauthVO;
import com.com2here.com2hereback.vo.UserLoginVO;

import jakarta.validation.Valid;

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
    }

    @PostMapping("/{provider}")
    public CMResponse<OauthVO> getOauthAccount(@PathVariable String provider,
            @Valid @RequestBody OauthReqDto oauthReqDto) {
        OauthRespDto oauthRespdto;
        switch (provider.toLowerCase()) {
            case "kakao":
                oauthRespdto = oauthService.getKakaoUserInfo(oauthReqDto.getCode());
                break;
            case "google":
                oauthRespdto = oauthService.getGoogleUserInfo(oauthReqDto.getCode());
                break;
            case "naver":
                oauthRespdto = oauthService.getNaverUserInfo(oauthReqDto.getCode());
                break;
            default:
                throw new BaseException(BaseResponseStatus.INVALID_PROVIDER);
        }

        OauthVO oauthVo = OauthVO.dtoToVo(oauthRespdto);
        return CMResponse.success(BaseResponseStatus.SUCCESS, oauthVo);
    }

    @PostMapping("/social-login")
    public CMResponse<UserLoginVO> socialLogin(@Valid @RequestBody SocialLoginReqDto socialLoginReqDto) {
        UserLoginRespDto userLoginRespDto = userService.registerOrLoginSocialUser(socialLoginReqDto);
        UserLoginVO userLoginVo = UserLoginVO.from(userLoginRespDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS, userLoginVo);
    }
}
