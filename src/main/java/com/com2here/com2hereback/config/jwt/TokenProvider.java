package com.com2here.com2hereback.config.jwt;

import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.repository.UserRepository;
import io.jsonwebtoken.io.Decoders;
import java.util.Date;

import javax.crypto.SecretKey;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

    private final UserRepository userRepository;

    @Getter
    private final SecretKey signingKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public TokenProvider(UserRepository userRepository, @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration-time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpirationTime) {
        secretKey = secretKey.replaceAll("\\s+", "");
        this.userRepository = userRepository;
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
        User user = userRepository.findByUuid(uuid);
        String role = user.getRole().name();

        Date expiryDate = new Date(System.currentTimeMillis() + expirationTime);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(uuid)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String tokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

            if (!claims.getExpiration().after(new Date())) {
                return false;
            }

            if (tokenType.equals("refresh")) {
                String uuid = claims.getSubject();
                User user = userRepository.findByUuid(uuid);
                if (user == null || !user.getRefreshToken().equals(token)) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        String subject = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return subject;
    }
}
