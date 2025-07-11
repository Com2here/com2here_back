package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductResponseDto;
import java.util.List;

public interface PcRecommendationService {
    List<ProductResponseDto> recommendPc(String purpose, List<String> programs, int budget);
}
