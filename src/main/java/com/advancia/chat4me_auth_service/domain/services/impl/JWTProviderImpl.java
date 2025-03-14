package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.services.JWTProvider;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Log4j2
@Component
public class JWTProviderImpl implements JWTProvider {
    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.jwt.duration}")
    private Duration jwtDuration;

    @Override
    public String generateJwt(UUID userId) {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtDuration.toMillis()))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    @Override
    public boolean validateJwt(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch(JwtException e) {
            log.error("validateJwt Exception: ", e);
            return false;
        }
    }
}