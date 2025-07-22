package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProgramDeleteReqDto {
    @NotBlank(message = "프로그램ID는 필수입니다.")
    private Long programId;
}
