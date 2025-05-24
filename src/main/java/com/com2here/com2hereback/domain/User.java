package com.com2here.com2hereback.domain;

import java.time.LocalDateTime;
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
    private String password;

    @Setter
    private String refreshToken;

    @Column(nullable = false)
    private boolean isEmailVerified;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String profileImageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    @Builder
    public User(Long user_id, String uuid, String nickname, String email, String password,
                String refreshToken, boolean isEmailVerified, Role role,
                String profileImageUrl, LocalDateTime createdAt, LocalDateTime lastLoginAt) {
        this.user_id = user_id;
        this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
        this.isEmailVerified = isEmailVerified;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.lastLoginAt = lastLoginAt;
    }
}

