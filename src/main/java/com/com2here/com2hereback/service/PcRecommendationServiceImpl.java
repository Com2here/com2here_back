package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.Program;
import com.com2here.com2hereback.domain.Cpu;
import com.com2here.com2hereback.domain.Gpu;
import com.com2here.com2hereback.dto.ProductRespDto;
import com.com2here.com2hereback.dto.RecommendReqDto;
import com.com2here.com2hereback.repository.ProgramRepository;
import com.com2here.com2hereback.repository.CpuRepository;
import com.com2here.com2hereback.repository.GpuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.com2here.com2hereback.util.Pair;

import java.util.*;


@Service
@RequiredArgsConstructor
public class PcRecommendationServiceImpl implements PcRecommendationService {

    private final ProgramRepository programRepository;
    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final NaverShoppingService naverShoppingService;

    @Override
    public List<ProductRespDto> recommendPc(RecommendReqDto request) {
        // 1. 입력된 프로그램들에 대해 최대 요구 사양 라인 구하기
        List<Program> programList = programRepository.findAll().stream()
                .filter(p -> request.getPrograms().stream()
                        .anyMatch(name -> p.getProgram().contains(name)))
                .toList();

        String maxLine = programList.stream()
                .map(Program::getSpecLevel)
                .map(this::extractLineFromSpec) // 예: "LINE: 하이엔드"
                .filter(line -> !line.isEmpty())
                .max(Comparator.comparingInt(this::getLinePriority))
                .orElse("로우엔드");

        // 2. maxLine 이상인 CPU, GPU 필터링
        List<Cpu> cpus = cpuRepository.findAll().stream()
                .filter(cpu -> isLineGreaterThanEqual(cpu.getLine(), maxLine))
                .toList();

        List<Gpu> gpus = gpuRepository.findAll().stream()
                .filter(gpu -> isLineGreaterThanEqual(gpu.getLine(), maxLine))
                .toList();

        List<ProductRespDto> results = new ArrayList<>();
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
        Set<String> deduplicationSet = new HashSet<>();

        for (int i = 0; i < Math.min(topPairs.size(), 50); i++) {
            Cpu cpu = topPairs.get(i).getFirst();
            Gpu gpu = topPairs.get(i).getSecond();

            String query = extractGpuKeyword(gpu.getChipset()) + " " + extractCpuKeyword(cpu.getModel());
            List<ProductRespDto> products = naverShoppingService.searchFilteredProducts(query, request.getBudget());

            for (ProductRespDto product : products) {
                if (product.getPrice() < 100000) continue;

                String normTitle = normalizeTitle(product.getTitle());
                String dedupKey = normTitle + "|" + product.getImage() + "|" + product.getPrice();

                if (deduplicationSet.contains(dedupKey)) continue;
                deduplicationSet.add(dedupKey);

                results.add(ProductRespDto.builder()
                        .productId(product.getProductId())
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

    private String normalizeTitle(String title) {
        return title.toLowerCase()
                .replaceAll("\\s+", "")               // 공백 제거
                .replaceAll("\\[[^\\]]*\\]", "")      // 대괄호 옵션 제거
                .replaceAll("[^a-zA-Z0-9가-힣]", ""); // 특수문자 제거
    }

    private boolean isLineGreaterThanEqual(String targetLine, String baseLine) {
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