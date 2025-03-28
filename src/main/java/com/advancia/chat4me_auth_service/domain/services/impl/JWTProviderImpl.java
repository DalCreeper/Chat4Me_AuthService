package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.services.JWTProvider;
import com.advancia.chat4me_auth_service.domain.services.SystemDateTimeProvider;
import com.advancia.chat4me_auth_service.domain.exceptions.JWTNotValidatedException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class JWTProviderImpl implements JWTProvider {
    private final SystemDateTimeProvider systemDateTimeProvider;
    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.jwt.duration}")
    private Duration jwtDuration;

    @Override
    public String generateJwt(UUID userId) {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date.from(systemDateTimeProvider.now().toInstant()))
            .setExpiration(Date.from(systemDateTimeProvider.now().plus(jwtDuration).toInstant()))
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
            log.info("JWT validated" + "\n");
            return true;
        } catch(JwtException e) {
            throw new JWTNotValidatedException(e);
        }
    }
}