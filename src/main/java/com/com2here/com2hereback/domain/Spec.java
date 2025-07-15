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
@Table(name = "spec")
public class Spec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_id")
    private Long specId;

    @Column(name = "cpu", nullable = false)
    private String cpu;

    @Column(name = "memory", nullable = false)
    private String memory;

    @Column(name = "graphic_card", nullable = false)
    private String graphicCard;

    @Column(name = "ssd", nullable = false)
    private String ssd;

    @Column(name = "main_board", nullable = false)
    private String mainBoard;

    @Column(name = "power", nullable = false)
    private String power;

    @Column(name = "case_name", nullable = false)
    private String caseName;

    @Builder
    public Spec(Long specId, String cpu, String memory, String graphicCard, String ssd, String mainBoard, String power, String caseId) {
        this.specId = specId;
        this.cpu = cpu;
        this.memory = memory;
        this.graphicCard = graphicCard;
        this.ssd = ssd;
        this.mainBoard = mainBoard;
        this.power = power;
        this.caseName = caseId;
    }
}
