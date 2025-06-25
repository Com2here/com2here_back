package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.dto.ProductResponseDto;
import com.com2here.com2hereback.dto.RecommendRequestDto;
import com.com2here.com2hereback.service.PcRecommendationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
public class PcRecommendationController {

    private final PcRecommendationService pcRecommendationService;

    @PostMapping
    public ResponseEntity<List<ProductResponseDto>> recommend(@RequestBody RecommendRequestDto request) {
        List<ProductResponseDto> result = pcRecommendationService.recommendPc(
            request.getPurpose(), request.getPrograms(), request.getBudget());
        return ResponseEntity.ok(result);
    }
}
