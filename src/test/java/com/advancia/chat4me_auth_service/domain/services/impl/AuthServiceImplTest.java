package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.ChallengeResponse;
import com.advancia.chat4me_auth_service.domain.model.LoginRequest;
import com.advancia.chat4me_auth_service.domain.model.OTPVerificationRequest;
import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.services.OTPProvider;
import com.advancia.chat4me_auth_service.domain.services.UUIDProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private AuthRepoService authRepoService;
    @Mock
    private OTPProvider otpProvider;
    @Mock
    private UUIDProvider uuidProvider;
    @Value("${app.secret-key}")
    private String secretKey;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authServiceImpl, "otpDuration", Duration.ofMinutes(5));
        ReflectionTestUtils.setField(authServiceImpl, "jwtDuration", Duration.ofDays(1));
    }

    @Test
    void shouldReturnChallengeResponseAndPrintOTP_whenAllOk() {
        LoginRequest loginRequest = LoginRequest.builder()
            .username("testUser")
            .password("testPassword")
            .build();
        User user = User.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username("testUser")
            .email("testEmail")
            .password("testPassword")
            .build();

        String fixedOtp = "123456";
        doReturn(fixedOtp).when(otpProvider).generateOtp();

        UUID uuid = UUID.randomUUID();
        doReturn(uuid).when(uuidProvider).generateUUID();

        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(uuid)
            .otp(fixedOtp)
            .expiresAt(Instant.now().plusSeconds(300).getEpochSecond())
            .userId(user.getId())
            .build();
        ChallengeResponse challengeResponse = ChallengeResponse.builder()
            .challengeId(otpVerificationRequest.getChallengeId())
            .message("Correct data - OTP sent")
            .userId(user.getId())
            .build();

        doReturn(Optional.of(user)).when(authRepoService).findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        doNothing().when(authRepoService).saveOTPVerificationRequest(otpVerificationRequest);

        ChallengeResponse challengeResponseResult = authServiceImpl.login(loginRequest);
        assertEquals(challengeResponse, challengeResponseResult);

        verify(authRepoService).findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        verify(authRepoService).saveOTPVerificationRequest(otpVerificationRequest);
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