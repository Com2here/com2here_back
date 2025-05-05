package com.com2here.com2hereback.domain;

import java.util.UUID;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(unique = true)
    private String uuid;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;  // 소셜 유저는 null 허용

    private String refreshToken;

    @Column(nullable = false)
    private boolean isEmailVerified;

    @Column(nullable = false)
    private String role;

    private String profileImageUrl;

    @Column(nullable = false)
    private boolean isSocial;  // 일반 유저: false, 소셜 유저: true

    @Builder
    public User(Long user_id, String uuid, String nickname, String email, String password,
                String refreshToken, boolean isEmailVerified, String role,
                String profileImageUrl, boolean isSocial) {
        this.user_id = user_id;
        this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
        this.isEmailVerified = isEmailVerified;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.isSocial = isSocial;
    }
}

