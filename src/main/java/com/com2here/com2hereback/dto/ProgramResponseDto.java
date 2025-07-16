package com.com2here.com2hereback.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProgramResponseDto {
    private Long id;
    private String program;
    private String specLevel;
    private String purpose;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}