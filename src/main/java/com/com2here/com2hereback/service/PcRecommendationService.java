package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductRespDto;
import com.com2here.com2hereback.dto.RecommendReqDto;

import java.util.List;

public interface PcRecommendationService {
    List<ProductRespDto> recommendPc(RecommendReqDto recommendReqDto);
}