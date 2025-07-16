package com.com2here.com2hereback.dto;

import com.com2here.com2hereback.domain.ProgramMSpec;
import com.com2here.com2hereback.domain.ProgramPurpose;
import com.com2here.com2hereback.domain.ProgramRSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramRequestDto {
    private String program;
    private String specLevel;
    private ProgramPurpose purpose;
    private ProgramMSpec pmSpec;
    private ProgramRSpec prSpec;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

