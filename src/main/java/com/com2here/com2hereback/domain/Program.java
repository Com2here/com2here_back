package com.com2here.com2hereback.domain;

import com.com2here.com2hereback.common.ProgramPurpose;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "program")
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long programId;

    @Column(name = "program", nullable = false)
    private String program;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false)
    private ProgramPurpose purpose;

    @Column(name = "spec_level", nullable = false)
    private String specLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_r_spec_id", nullable = false)
    private ProgramRSpec rSpec;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_m_spec_id", nullable = false)
    private ProgramMSpec mSpec;


    @Builder
    public Program(Long programId, String program, ProgramPurpose purpose,
        String specLevel, LocalDateTime createdAt, LocalDateTime updatedAt,
        ProgramRSpec rSpec, ProgramMSpec mSpec) {
        this.programId = programId;
        this.program = program;
        this.purpose = purpose;
        this.specLevel = specLevel;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt;
        this.rSpec = rSpec;
        this.mSpec = mSpec;
    }
}