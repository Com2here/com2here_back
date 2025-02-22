package com.com2here.com2hereback.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseVo {

    private String message;
    private String token;
}
