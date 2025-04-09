package com.com2here.com2hereback.config.jwt;

import io.jsonwebtoken.io.Decoders;
import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

    private final SecretKey signingKey;

    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration-time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpirationTime) {
        secretKey = secretKey.replaceAll("\\s+", "");
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String createAccessToken(String uuid) {
        return createToken(uuid, accessTokenExpirationTime);
    }

    public String createRefreshToken(String uuid) {
        return createToken(uuid, refreshTokenExpirationTime);
    }

    private String createToken(String uuid, long expirationTime) {
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTime);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }

    private boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUuidFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody(); // 클레임 반환
    }

    public String getSubject(String token) {
        String subject = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        try {
            return subject;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid subject format: " + subject, e);
        }
    }
}
