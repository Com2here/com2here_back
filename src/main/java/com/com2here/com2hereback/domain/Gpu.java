package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "gpu")
public class Gpu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_card_id")
    private Integer videoCardId;

    @Column(name = "chipset")
    private String chipset;

    @Column(name = "memory_gb")
    private Integer memoryGb;

    @Column(name = "core_clock_mhz")
    private Integer coreClockMhz;

    @Column(name = "boost_clock_mhz")
    private Integer boostClockMhz;

    @Column(name = "length_mm")
    private Integer lengthMm;

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
