package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepoService authRepoService;
    private static final String SECRET_KEY = "FeqVAAVPsmEUAlAXCNkNE3u1Sh4ksb2Jmc8QawzIDuE";

    @Override
    public ChallengeResponse login(LoginRequest loginRequest) {
        Optional<User> optionalUser = authRepoService.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if(optionalUser.isPresent()) {
            String otp = generateOtp();
            System.out.println("Generated OTP: " + otp + " for user: " + loginRequest.getUsername());
            return ChallengeResponse.builder()
                .challengeId(UUID.randomUUID())
                .message("Correct data")
                .userId(optionalUser.get().getId())
                .build();
        }
        return ChallengeResponse.builder().message("Invalid credentials").build();
    }

    @Override
    public AuthToken otpVerification(OTPVerificationRequest otpVerificationRequest) {
        String jwt = generateJwt(otpVerificationRequest.getUserId());
        return AuthToken.builder()
            .tokenId(UUID.randomUUID())
            .accessToken(jwt)
            .expiresIn(3600)
            .userId(otpVerificationRequest.getUserId())
            .build();
    }

    @Override
    public boolean tokenValidation(TokenValidationRequest tokenValidationRequest) {
        System.out.println("\nToken validation requested for: " + tokenValidationRequest.getAccessToken());
        if(validateJwt(tokenValidationRequest.getAccessToken())) {
            System.out.println("JWT validated" + "\n");
            return true;
        } else {
            System.out.println("JWT not validated" + "\n");
            return false;
        }
    }

    @Override
    public AuthToken refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String newJwt = generateJwt(refreshTokenRequest.getUserId());
        return AuthToken.builder()
            .tokenId(UUID.randomUUID())
            .accessToken(newJwt)
            .expiresIn(3600)
            .userId(refreshTokenRequest.getUserId())
            .build();
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private String generateJwt(UUID userId) {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    private boolean validateJwt(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch(JwtException e) {
            System.out.println(e);
            return false;
        }
    }
}