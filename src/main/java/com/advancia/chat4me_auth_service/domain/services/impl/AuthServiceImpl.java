package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepoService authRepoService;
    private final OTPProvider otpProvider;
    private final UUIDProvider uuidProvider;
    private final JWTProvider jwtProvider;
    private final SystemDateTimeProvider systemDateTimeProvider;

    @Value("${app.otp.duration}")
    private Duration otpDuration;

    @Value("${app.jwt.duration}")
    private Duration jwtDuration;

    @Override
    public ChallengeResponse login(LoginRequest loginRequest) {
        Optional<User> optionalUser = authRepoService.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            String otp = otpProvider.generateOtp();
            OTPVerificationRequest otpVerification = OTPVerificationRequest.builder()
                .challengeId(uuidProvider.generateUUID())
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
            int currentTimestamp = (int) systemDateTimeProvider.now().toEpochSecond();
            if(otpVerification.getExpiresAt() < currentTimestamp) {
                return AuthToken.builder().message("OTP expired").build();
            }
            if(!otpVerification.getOtp().equals(otpVerificationRequest.getOtp())) {
                return AuthToken.builder().message("Invalid OTP").build();
            }
            String jwt = jwtProvider.generateJwt(otpVerificationRequest.getUserId());
            AuthToken authToken = AuthToken.builder()
                .tokenId(uuidProvider.generateUUID())
                .expiresIn(jwtDuration.toMillis())
                .message("Auth token generated")
                .userId(otpVerificationRequest.getUserId())
                .build();
            authRepoService.saveAuthToken(authToken);
            authToken.setAccessToken(jwt);
            return authToken;
        }
        return AuthToken.builder().message("Challenge not found").build();
    }

    @Override
    public boolean tokenValidation(TokenValidationRequest tokenValidationRequest) {
        log.info("\nToken validation requested for: {}", tokenValidationRequest.getAccessToken());
        if(jwtProvider.validateJwt(tokenValidationRequest.getAccessToken())) {
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
            String newJwt = jwtProvider.generateJwt(refreshTokenRequest.getUserId());
            AuthToken newAuthToken = AuthToken.builder()
                .tokenId(uuidProvider.generateUUID())
                .expiresIn(jwtDuration.toMillis())
                .message("Auth token re-generated")
                .userId(refreshTokenRequest.getUserId())
                .build();
            authRepoService.saveAuthToken(newAuthToken);
            newAuthToken.setAccessToken(newJwt);
            return newAuthToken;
        }
        return AuthToken.builder().message("Refresh token not found").build();
    }
}