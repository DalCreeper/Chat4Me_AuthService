package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private AuthRepoService authRepoService;
    @Value("${app.secret-key}")
    private String secretKey;
    @Value("${app.otp.duration}")
    private Duration otpDuration;
    @Value("${app.jwt.duration}")
    private Duration jwtDuration;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Test
    void shouldReturnChallengeResponseAndPrintOTP_whenAllOk() {

    }

    @Test
    void shouldReturnChallengeResponse_whenLoginFails() {

    }

    @Test
    void shouldPropagateException_whenLoginAuthRepoServiceFails() {

    }

    @Test
    void shouldReturnAuthToken_whenOTPVerificationIsAllOk() {

    }

    @Test
    void shouldReturnAuthTokenWithOTPExpiredMessage_whenOTPVerificationFails() {

    }

    @Test
    void shouldReturnAuthTokenWithInvalidOTPMessage_whenOTPVerificationFails() {

    }

    @Test
    void shouldReturnAuthTokenWithChallengeNotFoundMessage_whenOTPVerificationFails() {

    }

    @Test
    void shouldPropagateException_whenOTPVerificationAuthRepoServiceFails() {

    }

    @Test
    void shouldReturnTrue_whenAllOk() {

    }

    @Test
    void shouldReturnFalse_whenTokenValidationFails() {

    }

    @Test
    void shouldReturnAuthToken_whenRefreshTokenIsAllOk() {

    }

    @Test
    void shouldReturnAuthTokenWithRefreshTokenNotFoundMessage_whenRefreshTokenFails() {

    }

    @Test
    void shouldPropagateException_whenRefreshTokenAuthRepoServiceFails() {

    }
}