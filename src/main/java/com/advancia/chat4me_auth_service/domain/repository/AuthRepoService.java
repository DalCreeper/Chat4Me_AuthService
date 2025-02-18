package com.advancia.chat4me_auth_service.domain.repository;

import com.advancia.chat4me_auth_service.domain.model.LoginRequest;
import com.advancia.chat4me_auth_service.domain.model.OTPVerificationRequest;
import com.advancia.chat4me_auth_service.domain.model.RefreshTokenRequest;
import com.advancia.chat4me_auth_service.domain.model.TokenValidationRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthRepoService {
    LoginRequest loginRequest();
    OTPVerificationRequest otpVerificationRequest();
    TokenValidationRequest tokenValidationRequest();
    RefreshTokenRequest refreshTokenRequest();
}
