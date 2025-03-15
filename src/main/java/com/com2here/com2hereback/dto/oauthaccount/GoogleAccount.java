package com.com2here.com2hereback.dto.oauthaccount;

import com.com2here.com2hereback.dto.Profile;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoogleAccount {
    private Profile profile;
    private String email;
    private String openid;
}