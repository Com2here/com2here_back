package com.com2here.com2hereback.vo;

import com.com2here.com2hereback.dto.UserShowRespDto;
import lombok.Value;

@Value
public class UserShowVO {
    String nickname;
    String email;
    String profileImageUrl;
    boolean isVerified;
    String role;

    public static UserShowVO from(UserShowRespDto dto) {
        return new UserShowVO(
            dto.getNickname(),
            dto.getEmail(),
            dto.getProfileImageUrl(),
            dto.isVerified(),
            dto.getRole()
        );
    }
}
