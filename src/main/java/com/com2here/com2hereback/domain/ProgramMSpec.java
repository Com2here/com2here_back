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
@Table(name = "program_m_spec")
public class ProgramMSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_m_spec_id")
    private Long pMSpecId;

    @Column(name = "cpu", nullable = false)
    private String cpu;

    @Column(name = "gpu", nullable = false)
    private String gpu;

    @Column(name = "ram", nullable = false)
    private int ram;

    @Column(name = "size", nullable = false)
    private double size;


    @Builder
    public ProgramMSpec(Long pMSpecId, String cpu, String gpu, int ram, double size) {
        this.pMSpecId = pMSpecId;
        this.cpu = cpu;
        this.gpu = gpu;
        this.ram = ram;
        this.size = size;
    }
}
