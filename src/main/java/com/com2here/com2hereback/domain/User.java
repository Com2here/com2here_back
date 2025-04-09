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

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String refreshToken;

    @Column(nullable = false)
    private boolean isEmailVerified;

    @Column(nullable = false)
    private boolean role;

    private String profileImageUrl;


    @Builder
    public User(Long user_id,String uuid, String nickname, String email, String password, String refreshToken, boolean isEmailVerified, boolean role, String profileImageUrl) {
        this.user_id = user_id;
        this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
        this.isEmailVerified = isEmailVerified;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }
}
