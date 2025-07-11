package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "cpu_detailed_matches")
public class CpuDetailedMatches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cpuId;

    private String model;
    private Integer cores;
    private Integer threads;
    private Float baseClockGhz;
    private Float boostClockGhz;
    private Integer tdpWatt;
    private String graphics;
    private LocalDateTime createdAt;
}
