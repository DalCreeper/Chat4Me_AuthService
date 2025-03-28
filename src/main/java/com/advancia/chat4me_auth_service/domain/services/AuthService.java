package com.advancia.chat4me_auth_service.domain.services;

import com.advancia.chat4me_auth_service.domain.model.*;

public interface AuthService {
    ChallengeResponse login(LoginRequest loginRequest);
    AuthToken otpVerification(OTPVerificationRequest otpVerificationRequest);
    boolean tokenValidation(TokenValidationRequest tokenValidationRequest);
    AuthToken refreshToken(RefreshTokenRequest refreshTokenRequest);
    User extractUUID(UserIdRequest userIDRequest);
}