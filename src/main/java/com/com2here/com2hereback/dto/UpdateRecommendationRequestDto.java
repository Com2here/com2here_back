package com.com2here.com2hereback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateRecommendationRequestDto {
    private String mainProgram;
    private String recommendedSpec;
    private String minimumSpec;
    private String purpose;
}
