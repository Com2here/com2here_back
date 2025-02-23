package com.com2here.com2hereback.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverAccount {
    private Profile profile;
    private String gender;
    private String birthday;
    private String email;
}