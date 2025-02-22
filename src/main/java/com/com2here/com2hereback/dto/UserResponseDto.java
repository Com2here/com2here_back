package com.com2here.com2hereback.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private String message;
    private String token;
}
