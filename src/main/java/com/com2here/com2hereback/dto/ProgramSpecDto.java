package com.com2here.com2hereback.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProgramSpecDto {
    @NotBlank(message = "CPU 정보는 필수입니다.")
    private String cpu;

    @NotBlank(message = "GPU 정보는 필수입니다.")
    private String gpu;

    @Min(value = 1, message = "RAM은 1 이상이어야 합니다.")
    private int ram;

    @Min(value = 1, message = "저장공간은 1 이상이어야 합니다.")
    private int size;

    public ProgramSpecDto(String minCpu, String minGpu, int minRam, double minSize) {}
}
