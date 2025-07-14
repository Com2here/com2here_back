package com.com2here.com2hereback.dto;

import lombok.Data;

@Data
public class SpecDto {
    private String cpu;
    private String gpu;
    private int ram;
    private double size;

    public SpecDto(String minCpu, String minGpu, int minRam, double minSize) {}
}
