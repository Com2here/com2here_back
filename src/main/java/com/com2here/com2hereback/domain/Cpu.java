package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "cpu")
public class Cpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cpuId;

    private String model;
    private Integer cores;
    private Integer threads;

    @Column(name = "base_clock_ghz")
    private Float baseClockGhz;

    @Column(name = "boost_clock_ghz")
    private Float boostClockGhz;

    @Column(name = "tdp_watt")
    private Integer tdpWatt;

    private String graphics;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private String line;
    private Integer price;

    @Column(name = "total_score")
    private Float totalScore;

    @Column(name = "pure_score")
    private Float pureScore;
}
