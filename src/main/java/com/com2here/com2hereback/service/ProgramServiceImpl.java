package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.domain.Program;
import com.com2here.com2hereback.domain.ProgramMSpec;
import com.com2here.com2hereback.common.ProgramPurpose;
import com.com2here.com2hereback.domain.ProgramRSpec;
import com.com2here.com2hereback.dto.ProgramRequestDto;
import com.com2here.com2hereback.dto.ProgramResponseDto;
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
    public void addProgram(ProgramRequestDto programReqDto) {
        if (programReqDto.getProgram() == null || programReqDto.getPurpose() == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        ProgramPurpose validatedPurpose = ProgramPurpose.from(programReqDto.getPurpose());
        if (validatedPurpose == null) {
            throw new BaseException(BaseResponseStatus.INVALID_PURPOSE);
        }

        ProgramMSpec mSpec = ProgramMSpec.builder()
            .cpu(programReqDto.getMinSpec().getCpu())
            .gpu(programReqDto.getMinSpec().getGpu())
            .ram(programReqDto.getMinSpec().getRam())
            .size(programReqDto.getMinSpec().getSize())
            .build();
        programMSpecRepository.save(mSpec);

        ProgramRSpec rSpec = ProgramRSpec.builder()
            .cpu(programReqDto.getRecSpec().getCpu())
            .gpu(programReqDto.getRecSpec().getGpu())
            .ram(programReqDto.getRecSpec().getRam())
            .size(programReqDto.getRecSpec().getSize())
            .build();
        programRSpecRepository.save(rSpec);

        Program program = Program.builder()
            .program(programReqDto.getProgram())
            .purpose(validatedPurpose)
            .specLevel(programReqDto.getSpecLevel())
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
        System.out.println("1");
        Page<Program> page = programRepository.findPage(search, programPurpose, pageable);
        System.out.println("24");
        // Entity → DTO 변환
        Page<ProgramResponseDto> dtoPage = page.map(ProgramResponseDto::new);
        System.out.println("23");
        Map<String, Object> result = new HashMap<>();
        result.put("content", dtoPage.getContent());
        result.put("totalElements", dtoPage.getTotalElements());
        result.put("totalPages", dtoPage.getTotalPages());
        result.put("pageNumber", dtoPage.getNumber());
        result.put("pageSize", dtoPage.getSize());
        result.put("isLast", dtoPage.isLast());
        System.out.println("2");
        return result;
    }

    @Override
    @Transactional
    public void updateProgram(ProgramRequestDto programReqDto) {

        if (programReqDto == null || programReqDto.isAllFieldsBlank()) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        Program program = programRepository.findById(programReqDto.getProgramId())
            .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_PROGRAM));

        ProgramPurpose purpose = programReqDto.getPurpose() == null
            ? program.getPurpose()
            : ProgramPurpose.from(programReqDto.getPurpose());

        if (purpose == null) {
            throw new BaseException(BaseResponseStatus.INVALID_PURPOSE);
        }

        ProgramMSpec ogMSpec = program.getMSpec();
        ProgramRSpec ogRSpec = program.getRSpec();

        ProgramMSpec updateMSpec = ProgramMSpec.builder()
            .pMSpecId(ogMSpec.getPMSpecId())
            .cpu(programReqDto.getMinSpec() != null && programReqDto.getMinSpec().getCpu()  != null ? programReqDto.getMinSpec().getCpu()  : ogMSpec.getCpu())
            .gpu(programReqDto.getMinSpec() != null && programReqDto.getMinSpec().getGpu()  != null ? programReqDto.getMinSpec().getGpu()  : ogMSpec.getGpu())
            .ram(programReqDto.getMinSpec() != null && programReqDto.getMinSpec().getRam()  != 0   ? programReqDto.getMinSpec().getRam()  : ogMSpec.getRam())
            .size(programReqDto.getMinSpec() != null && programReqDto.getMinSpec().getSize() != 0   ? programReqDto.getMinSpec().getSize() : ogMSpec.getSize())
            .build();
        programMSpecRepository.save(updateMSpec);

        ProgramRSpec updateRSpec = ProgramRSpec.builder()
            .pRSpecId(ogRSpec.getPRSpecId())
            .cpu(programReqDto.getRecSpec() != null && programReqDto.getRecSpec().getCpu()  != null ? programReqDto.getRecSpec().getCpu()  : ogRSpec.getCpu())
            .gpu(programReqDto.getRecSpec() != null && programReqDto.getRecSpec().getGpu()  != null ? programReqDto.getRecSpec().getGpu()  : ogRSpec.getGpu())
            .ram(programReqDto.getRecSpec() != null && programReqDto.getRecSpec().getRam()  != 0   ? programReqDto.getRecSpec().getRam()  : ogRSpec.getRam())
            .size(programReqDto.getRecSpec() != null && programReqDto.getRecSpec().getSize() != 0   ? programReqDto.getRecSpec().getSize() : ogRSpec.getSize())
            .build();
        programRSpecRepository.save(updateRSpec);

        Program updatedProgram = Program.builder()
            .programId(program.getProgramId())
            .program(programReqDto.getProgram()   != null ? programReqDto.getProgram()   : program.getProgram())
            .purpose(purpose)
            .specLevel(programReqDto.getSpecLevel() != null ? programReqDto.getSpecLevel() : program.getSpecLevel())
            .mSpec(updateMSpec)
            .rSpec(updateRSpec)
            .createdAt(program.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

        programRepository.save(updatedProgram);
    }

    @Override
    @Transactional
    public void deleteProgram(ProgramRequestDto programRequestDto) {
        Long programId = programRequestDto.getProgramId();

        if (programId == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }
        Program program = programRepository.findById(programId)
            .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_PROGRAM));

        programRepository.delete(program);
    }
}
