package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.UserTokenResponseDto;
import com.com2here.com2hereback.service.UserService;
import com.com2here.com2hereback.vo.UserTokenResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

    private final UserService userService;

    @GetMapping("/rotate")
    public CMResponse<UserTokenResponseVo> rotateToken() {
        try{
            UserTokenResponseDto userTokenResponseDto = userService.rotateToken();
            UserTokenResponseVo userTokenResponseVo = UserTokenResponseVo.dtoToVo(userTokenResponseDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, userTokenResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
