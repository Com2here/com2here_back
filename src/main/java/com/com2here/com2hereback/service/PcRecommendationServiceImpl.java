package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.Program;
import com.com2here.com2hereback.domain.Cpu;
import com.com2here.com2hereback.domain.Gpu;
import com.com2here.com2hereback.domain.ProgramPurpose;
import com.com2here.com2hereback.dto.ProductResponseDto;
import com.com2here.com2hereback.dto.RecommendRequestDto;
import com.com2here.com2hereback.repository.ProgramRepository;
import com.com2here.com2hereback.repository.CpuRepository;
import com.com2here.com2hereback.repository.GpuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.com2here.com2hereback.util.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class PcRecommendationServiceImpl implements PcRecommendationService {

    private final ProgramRepository programRepository;
    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final NaverShoppingService naverShoppingService;

    @Override
    public List<ProductResponseDto> recommendPc(RecommendRequestDto request) {
        System.out.println("▶ recommendPc() 호출됨");
        // 1. 입력된 프로그램들에 대해 최대 요구 사양 라인 구하기
        List<Program> programList = programRepository.findAll().stream()
                .filter(p -> request.getPrograms().stream()
                        .anyMatch(name -> p.getProgram().contains(name)))
                .toList();

        System.out.println("▶ 프로그램 목록 조회 완료: " + programList.size());

        String maxLine = programList.stream()
                .map(Program::getSpecLevel)
                .map(this::extractLineFromSpec) // 예: "LINE: 하이엔드"
                .filter(line -> !line.isEmpty())
                .max(Comparator.comparingInt(this::getLinePriority))
                .orElse("로우엔드");

        System.out.println("▶ 최대 요구 라인: " + maxLine);

        // 2. maxLine 이상인 CPU, GPU 필터링
        List<Cpu> cpus = cpuRepository.findAll().stream()
                .filter(cpu -> isLineGreaterThanEqual(cpu.getLine(), maxLine))
                .toList();

        System.out.println("▶ 필터링된 CPU 개수: " + cpus.size());

        List<Gpu> gpus = gpuRepository.findAll().stream()
                .filter(gpu -> isLineGreaterThanEqual(gpu.getLine(), maxLine))
                .toList();

        System.out.println("▶ 필터링된 GPU 개수: " + gpus.size());
        List<ProductResponseDto> results = new ArrayList<>();

        // 3. 동일 라인 조합만 추천
        List<Pair<Cpu, Gpu>> topPairs = new ArrayList<>();

        for (Cpu cpu : cpus) {
            for (Gpu gpu : gpus) {
                if (!cpu.getLine().equals(gpu.getLine())) continue;
                int totalPrice = cpu.getPrice() + gpu.getPrice();
                if (totalPrice > request.getBudget()) continue;
                topPairs.add(new Pair<>(cpu, gpu));
            }
        }

        // top N개 조합만 API 요청
        for (int i = 0; i < Math.min(topPairs.size(), 10); i++) {
            Cpu cpu = topPairs.get(i).getFirst();
            Gpu gpu = topPairs.get(i).getSecond();

            String query = extractGpuKeyword(gpu.getChipset()) + " " + extractCpuKeyword(cpu.getModel());
            System.out.println("💡 네이버 API 호출 시작: " + query);
            List<ProductResponseDto> products = naverShoppingService.searchFilteredProducts(query, request.getBudget());

            for (ProductResponseDto product : products) {
                if (product.getPrice() < 100000) continue;
                results.add(ProductResponseDto.builder()
                        .cpu(cpu.getModel())
                        .gpu(gpu.getChipset())
                        .line(cpu.getLine())
                        .totalScores(cpu.getTotalScore() + gpu.getTotalScore())
                        .totalPrice(cpu.getPrice() + gpu.getPrice())
                        .title(product.getTitle())
                        .link(product.getLink())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .mall(product.getMall())
                        .build());
            }
        }

        return results;
    }

    // 예: "CPU: 123, GPU: RTX 3060, LINE: 하이엔드" → "하이엔드"
    private String extractLineFromSpec(String specText) {
        return specText == null ? "" : specText.trim();
    }

    private int getLinePriority(String line) {
        if (line == null || line.isBlank()) return 0;
        return switch (line.trim()) {
            case "하이엔드" -> 4;
            case "퍼포먼스" -> 3;
            case "메인스트림" -> 2;
            case "로우엔드" -> 1;
            default -> 0;
        };
    }

    private boolean isLineGreaterThanEqual(String targetLine, String baseLine) {
        System.out.println("Comparing target: " + targetLine + " vs base: " + baseLine);
        if (targetLine == null || baseLine == null) return false;
        return getLinePriority(targetLine) >= getLinePriority(baseLine);
    }

    private String extractGpuKeyword(String raw) {
        if (raw == null) return "";
        return raw.replaceAll("(?i)geforce", "")
                .replaceAll("(?i)radeon", "")
                .trim().toLowerCase();
    }

    private String extractCpuKeyword(String raw) {
        if (raw == null) return "";
        String[] tokens = raw.trim().split("\\s+");
        for (int i = tokens.length - 1; i >= 0; i--) {
            if (tokens[i].matches("\\d+")) {
                return tokens[i];
            }
        }
        return "";
    }
}

