package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.ApiResponse;
import com.com2here.com2hereback.dto.ComputerRecommendationRequestDto;
import com.com2here.com2hereback.dto.ComputerRecommendationResponseDto;
import com.com2here.com2hereback.dto.UpdateRecommendationRequestDto;
import com.com2here.com2hereback.service.ComputerRecommendationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/computers")
public class ComputerRecommendationController {

    private final ComputerRecommendationService service;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateRecommendationRequest {
        private String mainProgram;
        private String recommendedSpec;
        private String minimumSpec;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addComputerRecommendation(
            @RequestBody @Valid ComputerRecommendationRequestDto dto
    ) {
        try {
            ComputerRecommendationResponseDto result = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "추천 항목이 성공적으로 등록되었습니다.", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "잘못된 요청 (필수 값 누락 또는 잘못된 입력)", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "서버 내부 오류가 발생했습니다.", null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateComputerRecommendation(
            @PathVariable Long id,
            @RequestBody UpdateRecommendationRequestDto request
    ) {
        try {
            ComputerRecommendationResponseDto updated = service.update(
                    id,
                    request.getMainProgram(),
                    request.getRecommendedSpec(),
                    request.getMinimumSpec(),
                    request.getPurpose()
            );
            return ResponseEntity.ok(new ApiResponse<>(200, "추천 항목이 성공적으로 수정되었습니다.", updated));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "수정할 데이터가 전달되지 않았습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "해당 ID의 추천 항목을 찾을 수 없습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.", null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/show")
    public ResponseEntity<?> getRecommendations(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        // limit 제한
        if (limit > 100) limit = 100;

        Map<String, Object> response = service.findAllWithPagination(offset, limit);

        Map<String, Object> pagination = (Map<String, Object>) response.get("pagination");
        int totalItems = ((Number) pagination.get("totalItems")).intValue();
        int totalPages = ((Number) pagination.get("totalPages")).intValue();

        int nextOffset = offset + limit;
        int prevOffset = Math.max(offset - limit, 0);

        Map<String, String> links = new LinkedHashMap<>();
        links.put("self", String.format("/recommendations?offset=%d&limit=%d", offset, limit));
        links.put("first", "/recommendations?offset=0&limit=" + limit);
        links.put("prev", offset > 0 ? String.format("/recommendations?offset=%d&limit=%d", prevOffset, limit) : null);
        links.put("next", nextOffset < totalItems ? String.format("/recommendations?offset=%d&limit=%d", nextOffset, limit) : null);
        links.put("last", String.format("/recommendations?offset=%d&limit=%d", ((totalPages - 1) * limit), limit));

        response.put("links", links);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComputerRecommendation(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new ApiResponse<>(204, "삭제에 성공했습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "해당 ID의 추천 항목을 찾을 수 없습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.", null));
        }
    }
}

