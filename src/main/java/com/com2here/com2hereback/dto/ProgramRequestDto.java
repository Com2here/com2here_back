package com.com2here.com2hereback.dto;

import lombok.Data;

@Data
public class ProgramRequestDto {
    private Long programId;
    private String program;
    private String purpose;
    private String specLevel;
    private SpecDto minSpec;
    private SpecDto recSpec;

    public boolean isAllFieldsBlank() {
        return (program == null || program.isBlank()) &&
            (purpose == null || purpose.isBlank()) &&
            (specLevel == null || specLevel.isBlank()) &&
            minSpec == null &&
            recSpec == null;
    }
}

