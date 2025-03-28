package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.exceptions.JWTNotValidatedException;
import com.advancia.chat4me_auth_service.domain.services.SystemDateTimeProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTProviderImplTest {
    private final String secretKey = "FeqVAAVPsmEUAlAXCNkNE3u1Sh4ksb2Jmc8QawzIDuE";
    private final Duration jwtDuration = Duration.ofDays(1);
    @Mock
    private Jws<Claims> jwsClaims;
    @Mock
    private SystemDateTimeProvider systemDateTimeProvider;
    @InjectMocks
    private JWTProviderImpl jwtProviderImpl;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtProviderImpl, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtProviderImpl, "jwtDuration", jwtDuration);
    }

    @Test
    void shouldReturnJWT_whenIsAllOk() {
        OffsetDateTime fixedCurrentTimestamp = OffsetDateTime.parse("2025-03-12T12:00:00.174779800+01:00");
        doReturn(fixedCurrentTimestamp).when(systemDateTimeProvider).now();
        Date fixedDateIssuedAt = Date.from(fixedCurrentTimestamp.toInstant());
        Date fixedDateExpiration = Date.from(systemDateTimeProvider.now().plus(jwtDuration).toInstant());
        UUID userId = UUID.randomUUID();
        String token = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(fixedDateIssuedAt)
            .setExpiration(fixedDateExpiration)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

        String tokenResult = jwtProviderImpl.generateJwt(userId);
        assertEquals(token, tokenResult);
    }

    @Test
    void shouldPropagateException_whenJWTBuilderFails() {
        ReflectionTestUtils.setField(jwtProviderImpl, "secretKey", null);
        UUID userId = UUID.randomUUID();

        assertThrows(RuntimeException.class, () -> jwtProviderImpl.generateJwt(userId));
    }

    @Test
    void shouldReturnTrue_whenJWTIsValid() {
        OffsetDateTime fixedCurrentTimestamp = OffsetDateTime.now();
        doReturn(fixedCurrentTimestamp).when(systemDateTimeProvider).now();
        Date fixedDateIssuedAt = Date.from(fixedCurrentTimestamp.toInstant());
        Date fixedDateExpiration = Date.from(systemDateTimeProvider.now().plus(jwtDuration).toInstant());
        UUID userId = UUID.randomUUID();
        String token = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(fixedDateIssuedAt)
            .setExpiration(fixedDateExpiration)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

        assertTrue(jwtProviderImpl.validateJwt(token));
    }

    @Test
    void shouldThrowException_whenJWTIsExpired() {
        OffsetDateTime pastTime = OffsetDateTime.now().minusMinutes(10);
        Date expiredDate = Date.from(pastTime.toInstant());
        UUID userId = UUID.randomUUID();
        String expiredToken = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date.from(pastTime.minusMinutes(1).toInstant()))
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

        assertThrows(JWTNotValidatedException.class, () -> jwtProviderImpl.validateJwt(expiredToken));
    }

    @Test
    void shouldThrowException_whenJWTIsInvalid() {
        String invalidToken = "invalid.jwt.token";
        assertThrows(JWTNotValidatedException.class, () -> jwtProviderImpl.validateJwt(invalidToken));
    }

    @Test
    void shouldReturnUserId_whenJWTIsValid() {
        OffsetDateTime fixedCurrentTimestamp = OffsetDateTime.now();
        doReturn(fixedCurrentTimestamp).when(systemDateTimeProvider).now();
        Date fixedDateIssuedAt = Date.from(fixedCurrentTimestamp.toInstant());
        Date fixedDateExpiration = Date.from(systemDateTimeProvider.now().plus(jwtDuration).toInstant());
        UUID userId = UUID.randomUUID();
        String token = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(fixedDateIssuedAt)
            .setExpiration(fixedDateExpiration)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

        UUID userIdResult = jwtProviderImpl.getUserIdFromJwt(token);
        assertEquals(userId, userIdResult);
    }

    @Test
    void shouldThrowException_whenJWTIsInvalidForUserIdExtraction() {
        String invalidToken = "invalid.jwt.token";
        assertThrows(JWTNotValidatedException.class, () -> jwtProviderImpl.getUserIdFromJwt(invalidToken));
    }
}