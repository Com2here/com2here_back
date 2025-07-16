package com.com2here.com2hereback.service;

import com.com2here.com2hereback.domain.Program;
import com.com2here.com2hereback.domain.ProgramPurpose;
import com.com2here.com2hereback.dto.ProgramRequestDto;
import com.com2here.com2hereback.dto.ProgramResponseDto;
import com.com2here.com2hereback.repository.ProgramRepository;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository repository;

    @Transactional
    public ProgramResponseDto create(ProgramRequestDto dto) {
        if (dto.getPurpose() == null || dto.getProgram() == null || dto.getSpecLevel() == null) {
            throw new IllegalArgumentException("모든 필드는 필수입니다.");
        }

        ProgramPurpose validatedPurpose;
        try {
            validatedPurpose = dto.getPurpose();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 용도입니다. [게임용, 작업용, 사무용, 개발용] 중 하나여야 합니다.");
        }

        Program saved = repository.save(
                Program.builder()
                        .program(dto.getProgram())
                        .specLevel(dto.getSpecLevel())
                        .purpose(validatedPurpose)
                        .pmSpec(dto.getPmSpec()) // FK
                        .prSpec(dto.getPrSpec()) // FK
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        return ProgramResponseDto.builder()
                .id(saved.getProgramId())
                .program(saved.getProgram())
                .purpose(saved.getPurpose().name())
                .specLevel(saved.getSpecLevel())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public Map<String, Object> findAllWithPagination(int offset, int limit, String search, String purpose) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("createdAt").descending());

        List<Program> all = repository.findAll(); // 임시

        // 필터링 적용
        List<Program> filtered = all.stream()
                .filter(r -> {
                    boolean matchesSearch = (search == null || search.isBlank()) ||
                            r.getProgram().contains(search) ||
                            r.getSpecLevel().contains(search);
                    boolean matchesPurpose = (purpose == null || purpose.isBlank()) ||
                            r.getPurpose().name().equalsIgnoreCase(purpose);
                    return matchesSearch && matchesPurpose;
                })
                .toList();

        // 페이징 수동 처리
        int fromIndex = Math.min(offset, filtered.size());
        int toIndex = Math.min(offset + limit, filtered.size());
        List<Program> pageContent = filtered.subList(fromIndex, toIndex);

        List<ProgramResponseDto> data = pageContent.stream()
                .map(entity -> ProgramResponseDto.builder()
                        .id(entity.getProgramId())
                        .program(entity.getProgram())
                        .purpose(entity.getPurpose().name())
                        .specLevel(entity.getSpecLevel())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());


        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", data);

        Map<String, Object> pagination = new LinkedHashMap<>();
        pagination.put("offset", offset);
        pagination.put("limit", limit);
        pagination.put("totalItems", filtered.size());
        pagination.put("totalPages", (int) Math.ceil((double) filtered.size() / limit));
        pagination.put("currentPage", (offset / limit) + 1);

        response.put("pagination", pagination);
        return response;
    }


    @Transactional
    public ProgramResponseDto update(Long id, String program, String specLevel, String purpose) {
        Program entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 추천 항목을 찾을 수 없습니다."));

        if ((program == null || program.isBlank()) &&
                (specLevel == null || specLevel.isBlank()) &&
                (purpose == null || purpose.isBlank())) {
            throw new IllegalStateException("수정할 데이터가 전달되지 않았습니다.");
        }

        if (program != null && !program.isBlank()) {
            entity.setProgram(program);
        }
        if (specLevel != null && !specLevel.isBlank()) {
            entity.setSpecLevel(specLevel);
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

        return ProgramResponseDto.builder()
                .id(entity.getProgramId())
                .program(entity.getProgram())
                .purpose(entity.getPurpose().name())
                .specLevel(entity.getSpecLevel())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    @Transactional
    public void delete(Long id) {
        Program entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 추천 항목을 찾을 수 없습니다."));
        repository.delete(entity);
    }
}
