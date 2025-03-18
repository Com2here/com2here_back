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
    private int user_id;

    @Column(nullable = false, unique = true)
    private String uuid;

    private String username;
    private String email;
    private String password;
    private String refreshToken;

    private boolean isEmailVerified;

    @Builder
    public User(int user_id,String uuid, String username, String email, String password, String refreshToken, boolean isEmailVerified) {
        this.user_id = user_id;
        this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
        this.isEmailVerified = isEmailVerified;
    }
}
