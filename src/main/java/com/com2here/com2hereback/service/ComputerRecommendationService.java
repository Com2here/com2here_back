package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.ComputerRecommendation;
import com.com2here.com2hereback.domain.ProgramPurpose;
import com.com2here.com2hereback.dto.ComputerRecommendationRequestDto;
import com.com2here.com2hereback.dto.ComputerRecommendationResponseDto;
import com.com2here.com2hereback.repository.ComputerRecommendationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComputerRecommendationService {

    private final ComputerRecommendationRepository repository;

    public ComputerRecommendationResponseDto create(ComputerRecommendationRequestDto dto) {
        if (dto.getPurpose() == null || dto.getMainProgram() == null
                || dto.getRecommendedSpec() == null || dto.getMinimumSpec() == null) {
            throw new IllegalArgumentException("모든 필드는 필수입니다.");
        }

        ProgramPurpose validatedPurpose;
        try {
            validatedPurpose = ProgramPurpose.valueOf(dto.getPurpose());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 용도입니다. [게임용, 작업용, 사무용, 개발용] 중 하나여야 합니다.");
        }

        ComputerRecommendation saved = repository.save(
                ComputerRecommendation.builder()
                        .mainProgram(dto.getMainProgram())
                        .purpose(validatedPurpose)
                        .recommendedSpec(dto.getRecommendedSpec())
                        .minimumSpec(dto.getMinimumSpec())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        return ComputerRecommendationResponseDto.builder()
                .id(saved.getComputer_id())
                .mainProgram(saved.getMainProgram())
                .purpose(saved.getPurpose().name())
                .recommendedSpec(saved.getRecommendedSpec())
                .minimumSpec(saved.getMinimumSpec())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public Map<String, Object> findAllWithPagination(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("createdAt").descending());
        Page<ComputerRecommendation> page = repository.findAll(pageable);

        List<ComputerRecommendationResponseDto> data = page.getContent().stream()
                .map(entity -> ComputerRecommendationResponseDto.builder()
                        .id(entity.getComputer_id())
                        .mainProgram(entity.getMainProgram())
                        .purpose(entity.getPurpose().name())
                        .recommendedSpec(entity.getRecommendedSpec())
                        .minimumSpec(entity.getMinimumSpec())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        // 응답 메타데이터 구성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", data);

        Map<String, Object> pagination = new LinkedHashMap<>();
        pagination.put("offset", offset);
        pagination.put("limit", limit);
        pagination.put("totalItems", page.getTotalElements());
        pagination.put("totalPages", page.getTotalPages());
        pagination.put("currentPage", page.getNumber() + 1);

        response.put("pagination", pagination);

        return response;
    }

    @Transactional
    public ComputerRecommendationResponseDto update(
            Long id,
            String mainProgram,
            String recommendedSpec,
            String minimumSpec,
            String purpose
    ) {
        ComputerRecommendation entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 추천 항목을 찾을 수 없습니다."));

        if ((mainProgram == null || mainProgram.isBlank()) &&
                (recommendedSpec == null || recommendedSpec.isBlank()) &&
                (minimumSpec == null || minimumSpec.isBlank()) &&
                (purpose == null || purpose.isBlank())) {
            throw new IllegalStateException("수정할 데이터가 전달되지 않았습니다.");
        }

        if (mainProgram != null && !mainProgram.isBlank()) {
            entity.setMainProgram(mainProgram);
        }
        if (recommendedSpec != null && !recommendedSpec.isBlank()) {
            entity.setRecommendedSpec(recommendedSpec);
        }
        if (minimumSpec != null && !minimumSpec.isBlank()) {
            entity.setMinimumSpec(minimumSpec);
        }
        if (purpose != null && !purpose.isBlank()) {
            try {
                ProgramPurpose validatedPurpose = ProgramPurpose.valueOf(purpose);
                entity.setPurpose(validatedPurpose);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 용도입니다. [게임용, 작업용, 사무용, 개발용] 중 하나여야 합니다.");
            }
        }

        entity.setUpdatedAt(LocalDateTime.now());

        return ComputerRecommendationResponseDto.builder()
                .id(entity.getComputer_id())
                .mainProgram(entity.getMainProgram())
                .purpose(entity.getPurpose().name())
                .recommendedSpec(entity.getRecommendedSpec())
                .minimumSpec(entity.getMinimumSpec())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Transactional
    public void delete(Long id) {
        ComputerRecommendation entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 추천 항목을 찾을 수 없습니다."));
        repository.delete(entity);
    }
}
