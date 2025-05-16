package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.ComputerRecommendation;
import com.com2here.com2hereback.dto.ComputerRecommendationRequestDto;
import com.com2here.com2hereback.dto.ComputerRecommendationResponseDto;
import com.com2here.com2hereback.repository.ComputerRecommendationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComputerRecommendationService {

    private final ComputerRecommendationRepository repository;

    public ComputerRecommendationResponseDto create(ComputerRecommendationRequestDto dto) {
        if (dto.getMainProgram() == null || dto.getRecommendedSpec() == null || dto.getMinimumSpec() == null) {
            throw new IllegalArgumentException("모든 필드는 필수입니다.");
        }

        ComputerRecommendation saved = repository.save(
                ComputerRecommendation.builder()
                        .mainProgram(dto.getMainProgram())
                        .recommendedSpec(dto.getRecommendedSpec())
                        .minimumSpec(dto.getMinimumSpec())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        return ComputerRecommendationResponseDto.builder()
                .id(saved.getComputer_id())
                .mainProgram(saved.getMainProgram())
                .recommendedSpec(saved.getRecommendedSpec())
                .minimumSpec(saved.getMinimumSpec())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public List<ComputerRecommendationResponseDto> findAll() {
        List<ComputerRecommendation> recommendations = repository.findAll();

        return recommendations.stream()
                .map(r -> ComputerRecommendationResponseDto.builder()
                        .id(r.getComputer_id())
                        .mainProgram(r.getMainProgram())
                        .recommendedSpec(r.getRecommendedSpec())
                        .minimumSpec(r.getMinimumSpec())
                        .createdAt(r.getCreatedAt())
                        .updatedAt(r.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public ComputerRecommendationResponseDto findById(Long id) {
        ComputerRecommendation entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 추천 항목입니다."));

        return ComputerRecommendationResponseDto.builder()
                .id(entity.getComputer_id())
                .mainProgram(entity.getMainProgram())
                .recommendedSpec(entity.getRecommendedSpec())
                .minimumSpec(entity.getMinimumSpec())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Transactional
    public ComputerRecommendationResponseDto update(
            Long id,
            String mainProgram,
            String recommendedSpec,
            String minimumSpec
    ) {
        ComputerRecommendation entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 추천 항목을 찾을 수 없습니다."));

        if ((mainProgram == null || mainProgram.isBlank()) &&
                (recommendedSpec == null || recommendedSpec.isBlank()) &&
                (minimumSpec == null || minimumSpec.isBlank())) {
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

        entity.setUpdatedAt(LocalDateTime.now());

        return ComputerRecommendationResponseDto.builder()
                .id(entity.getComputer_id())
                .mainProgram(entity.getMainProgram())
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
