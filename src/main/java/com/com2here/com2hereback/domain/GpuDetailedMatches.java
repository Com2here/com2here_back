package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "gpu_detailed_matches")
public class GpuDetailedMatches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer videoCardId;

    private String chipset;
    private Integer memoryGb;
    private Integer coreClockMhz;
    private Integer boostClockMhz;
    private Integer lengthMm;
    private LocalDateTime createdAt;
}
