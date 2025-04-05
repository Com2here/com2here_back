package com.com2here.com2hereback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Spec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spec_id;

    @Column(nullable = false)
    private String cpu;

    @Column(nullable = false)
    private String memory;

    @Column(nullable = false)
    private String graphicCard;

    @Column(nullable = false)
    private String ssd;

    @Column(nullable = false)
    private String mainBoard;

    @Column(nullable = false)
    private String power;

    @Column(nullable = false)
    private String caseName;

    @Builder
    public Spec(Long spec_id, String cpu, String memory, String graphicCard, String ssd, String mainBoard, String power, String caseId) {
        this.spec_id = spec_id;
        this.cpu = cpu;
        this.memory = memory;
        this.graphicCard = graphicCard;
        this.ssd = ssd;
        this.mainBoard = mainBoard;
        this.power = power;
        this.caseName = caseId;
    }
}
