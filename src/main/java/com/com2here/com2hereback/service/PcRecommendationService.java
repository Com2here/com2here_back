package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductResponseDto;
import com.com2here.com2hereback.dto.RecommendRequestDto;
import java.util.List;

public interface PcRecommendationService {
    List<ProductResponseDto> recommendPc(RecommendRequestDto request);
}