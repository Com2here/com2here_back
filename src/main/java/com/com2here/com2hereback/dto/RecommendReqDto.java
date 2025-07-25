package com.com2here.com2hereback.dto;

import java.util.List;
import lombok.Data;

@Data
public class RecommendReqDto {
    private String purpose;          // 예: "게임용", "사무용", "작업용", "개발용"
    private List<String> programs;   // 예: ["리그 오브 레전드", "FC 온라인"]
    private int budget;              // 예산 (원 단위), 예: 1200000
}
