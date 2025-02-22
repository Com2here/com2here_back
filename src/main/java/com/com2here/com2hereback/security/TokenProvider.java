package com.com2here.com2hereback.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.com2here.com2hereback.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {

    private final Key signingKey;
    private final long expirationTime;

    // application.properties에서 값을 가져옴
    public TokenProvider(@Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration-time}") long expirationTime) {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes()); // 보안 적용된 Key
        this.expirationTime = expirationTime;
    }

    // JWT 생성 메서드
    public String create(User userEntity) {
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setSubject(String.valueOf(userEntity.getUser_id()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS512) // 보안 적용된 서명 방식
                .compact();
    }

    // JWT 검증 및 userId 추출
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
