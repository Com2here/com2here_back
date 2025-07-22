package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.ProgramAddReqDto;
import com.com2here.com2hereback.dto.ProgramDeleteReqDto;
import com.com2here.com2hereback.dto.ProgramSearchReqDto;
import com.com2here.com2hereback.dto.ProgramUpdateReqDto;
import com.com2here.com2hereback.service.ProgramService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/program")
public class ProgramController {

    private final ProgramService programService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public CMResponse<Void> addProgram(@Valid @RequestBody ProgramAddReqDto programAddReqDto) {
        programService.addProgram(programAddReqDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/show")
    public CMResponse<Map<String, Object>> listPrograms(@Valid @RequestBody ProgramSearchReqDto programSearchReqDto) {
        Map<String, Object> page = programService.getProgram(programSearchReqDto.getPage(), programSearchReqDto.getLimit(), programSearchReqDto.getSearch(), programSearchReqDto.getPurpose());
        return CMResponse.success(BaseResponseStatus.SUCCESS, page);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update")
    public CMResponse<Void> updateProgram(@Valid @RequestBody ProgramUpdateReqDto programUpdateReqDto) {
        programService.updateProgram(programUpdateReqDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public CMResponse<Void> deleteProgram(@Valid @RequestBody ProgramDeleteReqDto programDeleteReqDto) {
        programService.deleteProgram(programDeleteReqDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }
}

