package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.dto.ProductRespDto;
import com.com2here.com2hereback.dto.RecommendReqDto;
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
    public ResponseEntity<List<ProductRespDto>> recommend(@RequestBody RecommendReqDto recommendReqDto) {
        List<ProductRespDto> result = pcRecommendationService.recommendPc(recommendReqDto);
        return ResponseEntity.ok(result);
    }
}
