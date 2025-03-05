package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepoService authRepoService;

    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.otp.duration}")
    private Duration otpDuration;

    @Value("${app.jwt.duration}")
    private Duration jwtDuration;

    @Override
    public ChallengeResponse login(LoginRequest loginRequest) {
        Optional<User> optionalUser = authRepoService.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            String otp = generateOtp();
            OTPVerificationRequest otpVerification = OTPVerificationRequest.builder()
                .challengeId(UUID.randomUUID())
                .otp(otp)
                .userId(user.getId())
                .expiresAt(Instant.now().plusMillis(otpDuration.toMillis()).getEpochSecond())
                .build();
            authRepoService.saveOTPVerificationRequest(otpVerification);
            log.info("\nGenerated OTP: {} for user: {}\n", otp, loginRequest.getUsername());
            return ChallengeResponse.builder()
                .challengeId(otpVerification.getChallengeId())
                .message("Correct data - OTP sent")
                .userId(user.getId())
                .build();
        }
        return ChallengeResponse.builder().message("Invalid credentials").build();
    }

    @Override
    public AuthToken otpVerification(OTPVerificationRequest otpVerificationRequest) {
        Optional<OTPVerificationRequest> otpRecord = authRepoService.findOTPById(otpVerificationRequest.getChallengeId());
        if(otpRecord.isPresent()) {
            OTPVerificationRequest otpVerification = otpRecord.get();
            int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
            if(otpVerification.getExpiresAt() < currentTimestamp) {
                return AuthToken.builder().message("OTP expired").build();
            }
            if(!otpVerification.getOtp().equals(otpVerificationRequest.getOtp())) {
                return AuthToken.builder().message("Invalid OTP").build();
            }
            String jwt = generateJwt(otpVerificationRequest.getUserId());
            AuthToken authToken = AuthToken.builder()
                .tokenId(UUID.randomUUID())
                .expiresIn(jwtDuration.toMillis())
                .message("Auth token generated")
                .userId(otpVerificationRequest.getUserId())
                .build();
            authRepoService.saveAuthToken(authToken);
            return AuthToken.builder().accessToken(jwt).build();
        }
        return AuthToken.builder().message("Challenge not found").build();
    }

    @Override
    public boolean tokenValidation(TokenValidationRequest tokenValidationRequest) {
        log.info("\nToken validation requested for: {}", tokenValidationRequest.getAccessToken());
        if(validateJwt(tokenValidationRequest.getAccessToken())) {
            log.info("JWT validated" + "\n");
            return true;
        } else {
            log.info("JWT not validated" + "\n");
            return false;
        }
    }

    @Override
    public AuthToken refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Optional<AuthToken> existingToken = authRepoService.findAuthById(refreshTokenRequest.getRefreshTokenId());
        if(existingToken.isPresent()) {
            String newJwt = generateJwt(refreshTokenRequest.getUserId());
            AuthToken newAuthToken = AuthToken.builder()
                .tokenId(UUID.randomUUID())
                .expiresIn(jwtDuration.toMillis())
                .message("Auth token re-generated")
                .userId(refreshTokenRequest.getUserId())
                .build();
            authRepoService.saveAuthToken(newAuthToken);
            return AuthToken.builder().accessToken(newJwt).build();
        }
        return AuthToken.builder().message("Refresh token not found").build();
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
            .setExpiration(new Date(System.currentTimeMillis() + jwtDuration.toMillis()))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    private boolean validateJwt(String token) {
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