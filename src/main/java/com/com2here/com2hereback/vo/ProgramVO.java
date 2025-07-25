package com.com2here.com2hereback.vo;

import lombok.Value;
import java.time.LocalDateTime;

import com.com2here.com2hereback.common.ProgramPurpose;
import com.com2here.com2hereback.domain.Program;

@Value
public class ProgramVO {
    Long id;
    String name;
    ProgramPurpose purpose;
    String specLevel;
    SpecVO recSpec;
    SpecVO minSpec;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static ProgramVO from(Program program) {
        return new ProgramVO(
            program.getProgramId(),
            program.getProgram(),
            program.getPurpose(),
            program.getSpecLevel(),
            new SpecVO(
                program.getRSpec().getCpu(),
                program.getRSpec().getGpu(),
                program.getRSpec().getRam(),
                program.getRSpec().getSize()
            ),
            new SpecVO(
                program.getMSpec().getCpu(),
                program.getMSpec().getGpu(),
                program.getMSpec().getRam(),
                program.getMSpec().getSize()
            ),
            program.getCreatedAt(),
            program.getUpdatedAt()
        );
    }
}
