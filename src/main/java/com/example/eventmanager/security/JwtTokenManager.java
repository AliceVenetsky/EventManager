package com.example.eventmanager.security;

import com.example.eventmanager.user.domain.User;
import com.example.eventmanager.user.domain.UserRole;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenManager {

    private final long lifeTime;
    private final SecretKey key;

    public JwtTokenManager(
            @Value("${jwt.lifetime}") long lifeTime,
            @Value("${jwt.secret-key}") String key
    ) {
        this.lifeTime = lifeTime;
        this.key = new SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
    }

    public String generateJwtToken(User user) {
        Date issueTime = new Date();
        Date expirationTime = new Date(issueTime.getTime() + lifeTime);
        Map<String, String> claims = new HashMap<>();
        claims.put("roles", user.role().name());
        return Jwts
                .builder()
                .expiration(expirationTime)
                .signWith(key, Jwts.SIG.HS256)
                .subject(user.login())
                .issuedAt(issueTime)
                .claims(claims)
                .compact();
    }

    public boolean isTokenValid(String jwtToken) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(jwtToken);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public String getLoginFromToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .get("roles", String.class);
    }
}
