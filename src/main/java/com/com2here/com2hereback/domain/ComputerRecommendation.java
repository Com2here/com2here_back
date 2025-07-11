package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "computer_recommendation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComputerRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long computer_id;

    @Column(nullable = false)
    private String mainProgram;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgramPurpose purpose;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String recommendedSpec;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String minimumSpec;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}