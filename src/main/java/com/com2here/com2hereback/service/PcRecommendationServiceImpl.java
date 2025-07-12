package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.Program;
import com.com2here.com2hereback.domain.Cpu;
import com.com2here.com2hereback.domain.Gpu;
import com.com2here.com2hereback.domain.ProgramPurpose;
import com.com2here.com2hereback.dto.ProductResponseDto;
import com.com2here.com2hereback.repository.ProgramRepository;
import com.com2here.com2hereback.repository.CpuRepository;
import com.com2here.com2hereback.repository.GpuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<ProductResponseDto> recommendPc(String purpose, List<String> programs, int budget) {
        Set<String> cpuKeywords = new HashSet<>();
        Set<String> gpuKeywords = new HashSet<>();



        for (String program : programs) {
            Optional<Program> optional = programRepository
                .findByMainProgramIgnoreCaseAndPurpose(program, ProgramPurpose.valueOf(purpose));
            if (optional.isPresent()) {
                Program rec = optional.get();
                SpecKeyword spec = parseSpec(rec.getRecommendedSpec());

//                String parsedCpu = extractCpuKeyword(spec.cpu());
//                String parsedGpu = extractGpuKeyword(spec.gpu());

//
//                if (!parsedCpu.isEmpty()) cpuKeywords.add(parsedCpu);
//                if (!parsedGpu.isEmpty()) gpuKeywords.add(parsedGpu);
                if (!spec.cpu().isEmpty()) cpuKeywords.add(spec.cpu());
                if (!spec.gpu().isEmpty()) gpuKeywords.add(spec.gpu());
            }
        }

        if (cpuKeywords.isEmpty() || gpuKeywords.isEmpty()) {
            return Collections.emptyList(); // 예외처리
        }

        List<Cpu> cpus = cpuRepository.findByModelIn(cpuKeywords);
        List<Gpu> gpus = gpuRepository.findByChipsetIn(gpuKeywords);

        List<String> queries = new ArrayList<>();
        for (Gpu gpu : gpus) {
            String gpuKeyword = extractGpuKeyword(gpu.getChipset());
            for (Cpu cpu : cpus) {
                String cpuKeyword = extractCpuKeyword(cpu.getModel());
                queries.add(gpuKeyword + " " + cpuKeyword);
                System.out.println(gpuKeyword + " " + cpuKeyword);
            }
        }

        List<ProductResponseDto> result = new ArrayList<>();
        for (String query : queries) {
            result.addAll(naverShoppingService.searchFilteredProducts(query, budget));
        }


        System.out.println("Programs: " + programs);
        System.out.println("CPU Keywords: " + cpuKeywords);
        System.out.println("GPU Keywords: " + gpuKeywords);
        System.out.println("Generated Queries: " + queries);

        return result;
    }

    // "GeForce RTX 4080" → "rtx 4080"
    private String extractGpuKeyword(String raw) {
        if (raw == null) return "";
        return raw
            .replaceAll("(?i)geforce", "")
            .replaceAll("(?i)radeon", "")
            .trim()
            .toLowerCase();
    }

    // "AMD Ryzen 5 7600" → "7600"
    private String extractCpuKeyword(String raw) {
        if (raw == null) return "";
        String[] tokens = raw.trim().split("\\s+");
        for (int i = tokens.length - 1; i >= 0; i--) {
            if (tokens[i].matches("\\d+")) {
                return tokens[i]; // 마지막 숫자 토큰
            }
        }
        return "";
    }


    /**
     * recommended_spec 문자열에서 CPU / GPU 추출
     * 예: "CPU: Ryzen 5 7600, GPU: GeForce RTX 4070"
     */
    private SpecKeyword parseSpec(String specText) {
        if (specText == null || specText.isBlank()) return new SpecKeyword("", "");

        Pattern pattern = Pattern.compile("CPU:\\s*(.*?),\\s*GPU:\\s*(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(specText);

        if (matcher.find()) {
            String cpu = matcher.group(1).trim();
            String gpu = matcher.group(2).trim();
            return new SpecKeyword(cpu, gpu);
        }

        return new SpecKeyword("", "");
    }

    /**
     * 사양 키워드 보관 클래스 (Java 16+ record 문법)
     */
    private record SpecKeyword(String cpu, String gpu) {}
}
