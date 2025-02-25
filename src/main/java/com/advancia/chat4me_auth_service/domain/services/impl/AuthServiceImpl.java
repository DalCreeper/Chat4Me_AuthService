package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.repository.AuthTokenRepository;
import com.advancia.chat4me_auth_service.domain.repository.OtpVerificationRepository;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LogManager.getLogger(AuthServiceImpl.class);
    private final AuthRepoService authRepoService;
    private final OtpVerificationRepository otpVerificationRepository;
    private final AuthTokenRepository authTokenRepository;
    private static final String SECRET_KEY = "FeqVAAVPsmEUAlAXCNkNE3u1Sh4ksb2Jmc8QawzIDuE";

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
                .expiresAt((int) (System.currentTimeMillis() / 1000) + 300)
                .build();
            otpVerificationRepository.save(otpVerification);
            System.out.println("\nGenerated OTP: " + otp + " for user: " + loginRequest.getUsername() + "\n");
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
        Optional<OTPVerificationRequest> otpRecord = otpVerificationRepository.findById(otpVerificationRequest.getChallengeId());
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
                .expiresIn(3600)
                .message("Auth token generated")
                .userId(otpVerificationRequest.getUserId())
                .build();
            authTokenRepository.save(authToken);
            return authToken.toBuilder().accessToken(jwt).build();
        }
        return AuthToken.builder().message("Challenge not found").build();
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
        Optional<AuthToken> existingToken = authTokenRepository.findById(refreshTokenRequest.getRefreshTokenId());
        if(existingToken.isPresent()) {
            String newJwt = generateJwt(refreshTokenRequest.getUserId());
            AuthToken newAuthToken = AuthToken.builder()
                .tokenId(UUID.randomUUID())
                .expiresIn(3600)
                .message("Auth token re-generated")
                .userId(refreshTokenRequest.getUserId())
                .build();
            authTokenRepository.save(newAuthToken);
            return newAuthToken.toBuilder().accessToken(newJwt).build();
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
            log.error("validateJwt Exception: ", e);
            return false;
        }
    }
}