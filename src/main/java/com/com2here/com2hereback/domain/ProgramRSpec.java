package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "program_r_spec")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramRSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_r_spec_id")
    private Long id;

    @Column(nullable = false)
    private String cpu;

    @Column(nullable = false)
    private String gpu;

    @Column(nullable = false)
    private int ram;

    @Column(nullable = false)
    private double size;
}
