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
public class PC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pc_id;

    private String image;

    @Column(nullable = false)
    private Long spec_id;

    @Column(nullable = false)
    private int price;

    @Builder
    public PC(Long pc_id, Long spec_id, String image, int price) {
        this.pc_id = pc_id;
        this.image = image;
        this.spec_id = spec_id;
        this.price = price;
    }
}
