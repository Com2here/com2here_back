package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.UserTokenResponseDto;

public interface TokenService {
    UserTokenResponseDto rotateToken();
}
