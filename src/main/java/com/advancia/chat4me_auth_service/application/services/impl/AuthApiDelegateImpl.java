package com.advancia.chat4me_auth_service.application.services.impl;

import com.advancia.Chat4Me_Auth_Service.generated.application.api.AuthApiDelegate;
import com.advancia.Chat4Me_Auth_Service.generated.application.model.*;
import com.advancia.chat4me_auth_service.application.mappers.AuthMappers;
import com.advancia.chat4me_auth_service.domain.model.AuthToken;
import com.advancia.chat4me_auth_service.domain.model.ChallengeResponse;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApiDelegateImpl implements AuthApiDelegate {
    private final AuthService authService;
    private final AuthMappers authMappers;

    @Override
    public ResponseEntity<ChallengeResponseDto> startLogin(LoginRequestDto loginRequestDto) {
        ChallengeResponse challengeResponse = authService.login(authMappers.convertToDomain(loginRequestDto));
        ChallengeResponseDto challengeResponseDto = authMappers.convertFromDomain(challengeResponse);
        return ResponseEntity.ok(challengeResponseDto);
    }

    @Override
    public ResponseEntity<AuthTokenDto> verifyOTP(OTPVerificationRequestDto otPVerificationRequestDto) {
        AuthToken authToken = authService.otpVerification(authMappers.convertToDomain(otPVerificationRequestDto));
        AuthTokenDto authTokenDto = authMappers.convertFromDomain(authToken);
        return ResponseEntity.ok(authTokenDto);
    }

    @Override
    public ResponseEntity<Void> validateToken(TokenValidationRequestDto tokenValidationRequestDto) {
        if(authService.tokenValidation(authMappers.convertToDomain(tokenValidationRequestDto))) {
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<AuthTokenDto> refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        AuthToken authToken = authService.refreshToken(authMappers.convertToDomain(refreshTokenRequestDto));
        AuthTokenDto newAuthTokenDto = authMappers.convertFromDomain(authToken);
        return ResponseEntity.ok(newAuthTokenDto);
    }
}