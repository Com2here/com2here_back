package com.com2here.com2hereback.domain;

import com.com2here.com2hereback.common.Role;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "uuid", unique = true)
    private String uuid;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Builder
    public User(Long userId, String uuid, String nickname, String email, String password,
                String refreshToken, boolean isEmailVerified, Role role,
                String profileImageUrl, LocalDateTime createdAt, LocalDateTime lastLoginAt) {
        this.userId = userId;
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

