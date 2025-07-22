package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.TokenRespDto;
import com.com2here.com2hereback.service.TokenService;
import com.com2here.com2hereback.vo.TokenVO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/rotate")
    public CMResponse<TokenVO> rotateToken() {
        TokenRespDto tokenRespDto = tokenService.rotateToken();
        TokenVO tokenVo = TokenVO.from(tokenRespDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS, tokenVo);
    }
}
