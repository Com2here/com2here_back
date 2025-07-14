package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.ProgramRequestDto;
import com.com2here.com2hereback.service.ProgramService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/program")
public class ProgramController {

    private final ProgramService programService;

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
    public CMResponse<Void> addProgram(@RequestBody ProgramRequestDto programReqDto) {
        try {
            programService.addProgram(programReqDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        }catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/show")
    public CMResponse<Map<String, Object>> getPrograms(
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String purpose
    ) {
        if (limit <= 0) limit = 10;  // 기본값 설정
        try {
            Map<String, Object> page = programService.getProgram(offset, limit, search, purpose);
            return CMResponse.success(BaseResponseStatus.SUCCESS, page);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        } catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update")
    public CMResponse<Void> updateProgram(@RequestBody ProgramRequestDto programReqDto) {
        try {
            programService.updateProgram(programReqDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        }catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public CMResponse<Void> deleteProgram(@RequestBody ProgramRequestDto programReqDto) {
        try {
            programService.deleteProgram(programReqDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        }catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

