package com.com2here.com2hereback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "program_r_spec")
public class ProgramRSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_r_spec_id")
    private Long pRSpecId;

    @Column(name = "cpu", nullable = false)
    private String cpu;

    @Column(name = "gpu", nullable = false)
    private String gpu;

    @Column(name = "ram", nullable = false)
    private int ram;

    @Column(name = "size", nullable = false)
    private double size;

    @Builder
    public ProgramRSpec(Long pRSpecId, String cpu, String gpu, int ram, double size) {
        this.pRSpecId = pRSpecId;
        this.cpu = cpu;
        this.gpu = gpu;
        this.ram = ram;
        this.size = size;
    }
}
