package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cpu")
public class Cpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpu_id")
    private Integer cpuId;

    @Column(name = "model")
    private String model;

    @Column(name = "cores")
    private Integer cores;

    @Column(name = "threads")
    private Integer threads;

    @Column(name = "base_clock_ghz")
    private Float baseClockGhz;

    @Column(name = "boost_clock_ghz")
    private Float boostClockGhz;

    @Column(name = "tdp_watt")
    private Integer tdpWatt;

    @Column(name = "graphics")
    private String graphics;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "line")
    private String line;

    @Column(name = "price")
    private Integer price;

    @Column(name = "total_score")
    private Float totalScore;

    @Column(name = "pure_score")
    private Float pureScore;

}
