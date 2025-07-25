package com.com2here.com2hereback.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginReqDto {
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider;
    private String oauthId;
}
