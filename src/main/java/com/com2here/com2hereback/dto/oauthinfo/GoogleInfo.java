package com.com2here.com2hereback.dto.oauthinfo;

import com.com2here.com2hereback.dto.oauthaccount.GoogleAccount;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleInfo {
    private GoogleAccount googleAccount;

    public static GoogleInfo fail() {
        return null;
    }
}