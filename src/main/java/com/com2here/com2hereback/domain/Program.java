package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "program")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long programId;

    @Column(nullable = false)
    private String program;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgramPurpose purpose;

    @Column(nullable = false, name = "spec_level")
    private String specLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_m_spec_id", nullable = false)
    private ProgramMSpec pmSpec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_r_spec_id", nullable = false)
    private ProgramRSpec prSpec;

    @Column(nullable = false, name = "created_at", columnDefinition = "DATETIME(6)")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at", columnDefinition = "DATETIME(6)")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
