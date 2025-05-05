package com.com2here.com2hereback.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String provider; // "kakao", "google", "naver"

    @Column(nullable = false, unique = true)
    private String oauthId;   // 소셜 플랫폼에서 제공하는 유저 고유 식별자

    @Builder
    public OauthAccount(User user, String provider, String oauthId) {
        this.user = user;
        this.provider = provider;
        this.oauthId = oauthId;
    }
}

