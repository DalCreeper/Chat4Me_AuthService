package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.exceptions.InvalidOTPException;
import com.advancia.chat4me_auth_service.domain.exceptions.OTPExpiredException;
import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

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
        User user = authRepoService.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        OTPVerificationRequest otpVerification = buildOTPVerificationRequest(user);
        authRepoService.saveOTPVerificationRequest(otpVerification);
        log.info("\nGenerated OTP: {} for user: {}\n", otpVerification.getOtp(), loginRequest.getUsername());
        return buildChallengeResponse(user, otpVerification);
    }

    @Override
    public AuthToken otpVerification(OTPVerificationRequest otpVerificationRequest) {
        OTPVerificationRequest otpRecord = authRepoService.findOTPById(otpVerificationRequest.getChallengeId());
        int currentTimestamp = (int) systemDateTimeProvider.now().toEpochSecond();
        if(otpRecord.getExpiresAt() < currentTimestamp) {
            throw buildOTPExpiredException();
        }
        if(!otpRecord.getOtp().equals(otpVerificationRequest.getOtp())) {
            throw buildInvalidOTPException();
        }
        String jwt = jwtProvider.generateJwt(otpVerificationRequest.getUserId());
        AuthToken authToken = buildAuthToken(otpVerificationRequest.getUserId(), "Auth token generated");
        authRepoService.saveAuthToken(authToken);
        authToken.setAccessToken(jwt);
        return authToken;
    }

    @Override
    public boolean tokenValidation(TokenValidationRequest tokenValidationRequest) {
        log.info("\nToken validation requested for: {}", tokenValidationRequest.getAccessToken());
        return jwtProvider.validateJwt(tokenValidationRequest.getAccessToken());
    }

    @Override
    public AuthToken refreshToken(RefreshTokenRequest refreshTokenRequest) {
        authRepoService.findAuthById(refreshTokenRequest.getRefreshTokenId());
        String newJwt = jwtProvider.generateJwt(refreshTokenRequest.getUserId());
        AuthToken newAuthToken = buildAuthToken(refreshTokenRequest.getUserId(), "Auth token re-generated");
        authRepoService.saveAuthToken(newAuthToken);
        newAuthToken.setAccessToken(newJwt);
        return newAuthToken;
    }

    private OTPVerificationRequest buildOTPVerificationRequest(User user) {
        String otp = otpProvider.generateOtp();
        return OTPVerificationRequest.builder()
            .challengeId(uuidProvider.generateUUID())
            .otp(otp)
            .userId(user.getId())
            .expiresAt(Instant.now().plusMillis(otpDuration.toMillis()).getEpochSecond())
            .build();
    }

    public OTPExpiredException buildOTPExpiredException() {
        return new OTPExpiredException();
    }

    public InvalidOTPException buildInvalidOTPException() {
        return new InvalidOTPException();
    }

    private ChallengeResponse buildChallengeResponse(User user, OTPVerificationRequest otpVerificationRequest) {
        return ChallengeResponse.builder()
            .challengeId(otpVerificationRequest.getChallengeId())
            .message("Correct data - OTP sent")
            .userId(user.getId())
            .build();
    }

    private AuthToken buildAuthToken(UUID userId, String message) {
        return AuthToken.builder()
            .tokenId(uuidProvider.generateUUID())
            .expiresIn(jwtDuration.toMillis())
            .message(message)
            .userId(userId)
            .build();
    }
}