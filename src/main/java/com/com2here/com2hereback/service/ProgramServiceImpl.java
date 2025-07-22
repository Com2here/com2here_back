package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.domain.Program;
import com.com2here.com2hereback.domain.ProgramMSpec;
import com.com2here.com2hereback.common.ProgramPurpose;
import com.com2here.com2hereback.domain.ProgramRSpec;
import com.com2here.com2hereback.dto.ProgramUpdateReqDto;
import com.com2here.com2hereback.dto.ProgramAddReqDto;
import com.com2here.com2hereback.dto.ProgramDeleteReqDto;
import com.com2here.com2hereback.dto.ProgramRespDto;
import com.com2here.com2hereback.repository.ProgramMSpecRepository;
import com.com2here.com2hereback.repository.ProgramRSpecRepository;
import com.com2here.com2hereback.repository.ProgramRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgramServiceImpl implements ProgramService {
    private final ProgramRepository programRepository;
    private final ProgramMSpecRepository programMSpecRepository;
    private final ProgramRSpecRepository programRSpecRepository;

    @Override
    @Transactional
    public void addProgram(ProgramAddReqDto programAddReqDto) {

        ProgramPurpose validatedPurpose = ProgramPurpose.from(programAddReqDto.getPurpose());
        if (validatedPurpose == null) {
            throw new BaseException(BaseResponseStatus.INVALID_PURPOSE);
        }

        ProgramMSpec mSpec = ProgramMSpec.builder()
            .cpu(programAddReqDto.getMinSpec().getCpu())
            .gpu(programAddReqDto.getMinSpec().getGpu())
            .ram(programAddReqDto.getMinSpec().getRam())
            .size(programAddReqDto.getMinSpec().getSize())
            .build();
        programMSpecRepository.save(mSpec);

        ProgramRSpec rSpec = ProgramRSpec.builder()
            .cpu(programAddReqDto.getRecSpec().getCpu())
            .gpu(programAddReqDto.getRecSpec().getGpu())
            .ram(programAddReqDto.getRecSpec().getRam())
            .size(programAddReqDto.getRecSpec().getSize())
            .build();
        programRSpecRepository.save(rSpec);

        Program program = Program.builder()
            .program(programAddReqDto.getProgram())
            .purpose(validatedPurpose)
            .specLevel(programAddReqDto.getSpecLevel())
            .rSpec(rSpec)
            .mSpec(mSpec)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        programRepository.save(program);
    }

    @Override
    public Map<String, Object> getProgram(int offset, int limit, String search, String purpose) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("createdAt").descending());

        ProgramPurpose programPurpose = null;
        if (purpose != null && !purpose.isBlank()) {
            programPurpose = ProgramPurpose.valueOf(purpose);
        }
        Page<Program> page = programRepository.findPage(search, programPurpose, pageable);

        Page<ProgramRespDto> dtoPage = page.map(ProgramRespDto::new);

        Map<String, Object> result = new HashMap<>();
        result.put("content", dtoPage.getContent());
        result.put("totalElements", dtoPage.getTotalElements());
        result.put("totalPages", dtoPage.getTotalPages());
        result.put("pageNumber", dtoPage.getNumber());
        result.put("pageSize", dtoPage.getSize());
        result.put("isLast", dtoPage.isLast());
        return result;
    }

    @Override
    @Transactional
    public void updateProgram(ProgramUpdateReqDto programUpdateReqDto) {

        Program program = programRepository.findById(programUpdateReqDto.getProgramId())
            .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_PROGRAM));

        ProgramPurpose purpose = programUpdateReqDto.getPurpose() == null
            ? program.getPurpose()
            : ProgramPurpose.from(programUpdateReqDto.getPurpose());

        if (purpose == null) {
            throw new BaseException(BaseResponseStatus.INVALID_PURPOSE);
        }

        // 최소 사양 업데이트
        ProgramMSpec updateMSpec = ProgramMSpec.builder()
            .pMSpecId(program.getMSpec().getPMSpecId())
            .cpu(programUpdateReqDto.getMinSpec().getCpu())
            .gpu(programUpdateReqDto.getMinSpec().getGpu())
            .ram(programUpdateReqDto.getMinSpec().getRam())
            .size(programUpdateReqDto.getMinSpec().getSize())
            .build();
        programMSpecRepository.save(updateMSpec);

        // 권장 사양 업데이트
        ProgramRSpec updateRSpec = ProgramRSpec.builder()
            .pRSpecId(program.getRSpec().getPRSpecId())
            .cpu(programUpdateReqDto.getRecSpec().getCpu())
            .gpu(programUpdateReqDto.getRecSpec().getGpu())
            .ram(programUpdateReqDto.getRecSpec().getRam())
            .size(programUpdateReqDto.getRecSpec().getSize())
            .build();
        programRSpecRepository.save(updateRSpec);

        // Program 업데이트
        Program updatedProgram = Program.builder()
            .programId(program.getProgramId())
            .program(programUpdateReqDto.getProgram())
            .purpose(purpose)
            .specLevel(programUpdateReqDto.getSpecLevel())
            .mSpec(updateMSpec)
            .rSpec(updateRSpec)
            .createdAt(program.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();
            programRepository.save(updatedProgram);
        }

    @Override
    @Transactional
    public void deleteProgram(ProgramDeleteReqDto programDeleteReqDto) {
        Long programId = programDeleteReqDto.getProgramId();

        Program program = programRepository.findById(programId)
            .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_PROGRAM));

        programRepository.delete(program);
    }
}
