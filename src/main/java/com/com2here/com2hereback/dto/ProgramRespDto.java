package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.Program;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProgramRespDto {
    private String program;
    private String purpose;
    private String specLevel;
    private ProgramSpecDto minSpec;
    private ProgramSpecDto recSpec;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProgramRespDto(Program p) {
        this.program = p.getProgram();
        this.purpose = (p.getPurpose() != null) ? p.getPurpose().toString() : null;
        this.specLevel = p.getSpecLevel();

        if (p.getMSpec() != null) {
            this.minSpec = new ProgramSpecDto(
                p.getMSpec().getCpu(),
                p.getMSpec().getGpu(),
                p.getMSpec().getRam(),
                p.getMSpec().getSize()
            );
        } else {
            this.minSpec = null;
        }

        if (p.getRSpec() != null) {
            this.recSpec = new ProgramSpecDto(
                p.getRSpec().getCpu(),
                p.getRSpec().getGpu(),
                p.getRSpec().getRam(),
                p.getRSpec().getSize()
            );
        } else {
            this.recSpec = null;
        }

        this.createdAt = p.getCreatedAt();
        this.updatedAt = p.getUpdatedAt();
    }

}
