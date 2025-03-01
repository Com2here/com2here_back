package com.com2here.com2hereback.security;

import io.jsonwebtoken.io.Decoders;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.com2here.com2hereback.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

    // 비밀키 생성 (실제 운영환경에서는 설정 파일에서 관리하는 것을 권장)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final SecretKey signingKey;
    private final long expirationTime;

    // application.properties에서 값을 가져옴
    public TokenProvider(@Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration-time}") long expirationTime) {
        // this.signingKey =
        // Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));; // 보안 적용된
        // Key
        secretKey = secretKey.replaceAll("\\s+", "");
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.expirationTime = expirationTime;
    }

    // JWT 생성 메서드
    public String create(User user) {
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(user.getUser_id()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256) // 보안 적용된 서명 방식
                .compact();

    }

    // jwt 검증 메소드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // JWT 디코딩 메서드
    public Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody(); // 클레임 반환
    }

    // 토큰에서 값 추출
    public int getSubject(String token) {
        String subject = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        // String에서 int로 변환
        try {
            return Integer.parseInt(subject); // 변환 성공
        } catch (NumberFormatException e) {
            // 변환 실패 시 처리 (예: 로그 남기기, 예외 던지기 등)
            throw new IllegalArgumentException("Invalid subject format: " + subject, e);
        }
    }
}
