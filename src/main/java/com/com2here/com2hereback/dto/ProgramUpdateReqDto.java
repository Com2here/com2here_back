package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProgramUpdateReqDto {
    @NotBlank(message = "프로그램ID는 필수입니다.")
    private Long programId;

    @NotBlank(message = "프로그램명은 필수입니다.")
    private String program;

    @NotBlank(message = "목적은 필수입니다.")
    private String purpose;

    @NotBlank(message = "스펙레벨은 필수입니다.")
    private String specLevel;

    @NotNull(message = "최소 사양은 필수입니다.")
    private ProgramSpecDto minSpec;

    @NotNull(message = "권장 사양은 필수입니다.")
    private ProgramSpecDto recSpec;
}

