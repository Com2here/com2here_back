package com.com2here.com2hereback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComputerRecommendationRequestDto {
    private String mainProgram;
    private String purpose;
    private String recommendedSpec;
    private String minimumSpec;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

