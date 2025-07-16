package com.advancia.chat4me_auth_service.application;

import com.advancia.chat4me_auth_service.generated.application.api.AuthApiDelegate;
import com.advancia.chat4me_auth_service.generated.application.model.*;
import com.advancia.chat4me_auth_service.application.mappers.AuthMappers;
import com.advancia.chat4me_auth_service.application.mappers.UserMappers;
import com.advancia.chat4me_auth_service.domain.model.AuthToken;
import com.advancia.chat4me_auth_service.domain.model.ChallengeResponse;
import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApiDelegateImpl implements AuthApiDelegate {
    private final AuthService authService;
    private final AuthMappers authMappers;
    private final UserMappers userMappers;

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
        authService.tokenValidation(authMappers.convertToDomain(tokenValidationRequestDto));
        return ResponseEntity.ok().body(null);
    }

    @Override
    public ResponseEntity<AuthTokenDto> refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        AuthToken authToken = authService.refreshToken(authMappers.convertToDomain(refreshTokenRequestDto));
        AuthTokenDto newAuthTokenDto = authMappers.convertFromDomain(authToken);
        return ResponseEntity.ok(newAuthTokenDto);
    }

    @Override
    public ResponseEntity<UserDto> extractUUID(UserIdRequestDto userIdRequestDto) {
        User user = authService.extractUUID(authMappers.convertToDomain(userIdRequestDto));
        UserDto userDto = userMappers.convertFromDomain(user);
        return ResponseEntity.ok(userDto);
    }
}