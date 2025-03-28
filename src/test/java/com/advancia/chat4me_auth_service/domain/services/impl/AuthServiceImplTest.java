package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.exceptions.InvalidOTPException;
import com.advancia.chat4me_auth_service.domain.exceptions.OTPExpiredException;
import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepoService;
import com.advancia.chat4me_auth_service.domain.services.JWTProvider;
import com.advancia.chat4me_auth_service.domain.services.OTPProvider;
import com.advancia.chat4me_auth_service.domain.services.SystemDateTimeProvider;
import com.advancia.chat4me_auth_service.domain.services.UUIDProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private AuthRepoService authRepoService;
    @Mock
    private UsersRepoService usersRepoService;
    @Mock
    private OTPProvider otpProvider;
    @Mock
    private UUIDProvider uuidProvider;
    @Mock
    private JWTProvider jwtProvider;
    @Mock
    private SystemDateTimeProvider systemDateTimeProvider;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authServiceImpl, "otpDuration", Duration.ofMinutes(5));
        ReflectionTestUtils.setField(authServiceImpl, "jwtDuration", Duration.ofDays(1));
    }

    @Test
    void shouldReturnChallengeResponseAndPrintOTP_whenIsAllOk() {
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

        doReturn(user).when(authRepoService).findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        doNothing().when(authRepoService).saveOTPVerificationRequest(otpVerificationRequest);

        ChallengeResponse challengeResponseResult = authServiceImpl.login(loginRequest);
        assertEquals(challengeResponse, challengeResponseResult);

        verify(authRepoService).findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        verify(authRepoService).saveOTPVerificationRequest(otpVerificationRequest);
    }

    @Test
    void shouldPropagateException_whenLoginAuthRepoServiceFails() {
        LoginRequest loginRequest = LoginRequest.builder()
            .username("testUser")
            .password("testPassword")
            .build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(authRepoService).findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());

        Exception ex = assertThrows(RuntimeException.class, () -> authServiceImpl.login(loginRequest));
        assertSame(runtimeException, ex);

        verify(authRepoService).findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        verify(authRepoService, never()).saveOTPVerificationRequest(any(OTPVerificationRequest.class));
    }

    @Test
    void shouldReturnAuthToken_whenOTPVerificationIsAllOk() {
        OffsetDateTime fixedCurrentTimestamp = OffsetDateTime.parse("2025-03-12T12:00:00.174779800+01:00");
        doReturn(fixedCurrentTimestamp).when(systemDateTimeProvider).now();
        int fixedExpires = (int) fixedCurrentTimestamp.toEpochSecond();

        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(fixedExpires)
            .userId(UUID.randomUUID())
            .build();
        OTPVerificationRequest otpRecord = OTPVerificationRequest.builder()
            .challengeId(otpVerificationRequest.getChallengeId())
            .otp(otpVerificationRequest.getOtp())
            .expiresAt(otpVerificationRequest.getExpiresAt())
            .userId(otpVerificationRequest.getUserId())
            .build();

        UUID uuid = UUID.randomUUID();
        doReturn(uuid).when(uuidProvider).generateUUID();

        AuthToken authToken = AuthToken.builder()
            .tokenId(uuid)
            .expiresIn(86400000L) // 1 day
            .message("Auth token generated")
            .userId(otpVerificationRequest.getUserId())
            .build();

        doReturn(otpRecord).when(authRepoService).findOTPById(otpVerificationRequest.getChallengeId());
        assertFalse(otpVerificationRequest.getExpiresAt() < fixedExpires);
        assertEquals(otpRecord.getOtp(), otpVerificationRequest.getOtp());
        doNothing().when(authRepoService).saveAuthToken(authToken);

        AuthToken authTokenResult = authServiceImpl.otpVerification(otpVerificationRequest);
        assertEquals(authToken, authTokenResult);

        verify(authRepoService).findOTPById(otpVerificationRequest.getChallengeId());
        verify(authRepoService).saveAuthToken(authToken);
    }

    @Test
    void shouldReturnAuthTokenWithOTPExpiredMessage_whenOTPVerificationFails() {
        OffsetDateTime fixedCurrentTimestamp = OffsetDateTime.parse("2025-03-12T12:00:00.174779800+01:00");
        doReturn(fixedCurrentTimestamp).when(systemDateTimeProvider).now();
        int fixedExpires = (int) fixedCurrentTimestamp.toEpochSecond();

        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(fixedExpires - 1)
            .userId(UUID.randomUUID())
            .build();
        OTPVerificationRequest otpRecord = OTPVerificationRequest.builder()
            .challengeId(otpVerificationRequest.getChallengeId())
            .otp(otpVerificationRequest.getOtp())
            .expiresAt(otpVerificationRequest.getExpiresAt())
            .userId(otpVerificationRequest.getUserId())
            .build();

        doReturn(otpRecord).when(authRepoService).findOTPById(otpVerificationRequest.getChallengeId());
        assertTrue(otpVerificationRequest.getExpiresAt() < fixedExpires);

        AuthServiceImpl authServiceSpy = Mockito.spy(authServiceImpl);
        doThrow(new OTPExpiredException()).when(authServiceSpy).buildOTPExpiredException();
        assertThrows(OTPExpiredException.class, () -> authServiceSpy.otpVerification(otpVerificationRequest));

        verify(authRepoService).findOTPById(otpVerificationRequest.getChallengeId());
        verify(authRepoService, never()).saveAuthToken(any(AuthToken.class));
    }

    @Test
    void shouldThrowInvalidOTPException_whenOTPVerificationFails() {
        OffsetDateTime fixedCurrentTimestamp = OffsetDateTime.parse("2025-03-12T12:00:00.174779800+01:00");
        doReturn(fixedCurrentTimestamp).when(systemDateTimeProvider).now();
        int fixedExpires = (int) fixedCurrentTimestamp.toEpochSecond();

        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
                .challengeId(UUID.randomUUID())
                .otp("123456")
                .expiresAt(fixedExpires)
                .userId(UUID.randomUUID())
                .build();
        OTPVerificationRequest otpRecord = OTPVerificationRequest.builder()
                .challengeId(otpVerificationRequest.getChallengeId())
                .otp("012345")
                .expiresAt(otpVerificationRequest.getExpiresAt())
                .userId(otpVerificationRequest.getUserId())
                .build();

        doReturn(otpRecord).when(authRepoService).findOTPById(otpVerificationRequest.getChallengeId());
        assertFalse(otpVerificationRequest.getExpiresAt() < fixedExpires);
        assertNotEquals(otpRecord.getOtp(), otpVerificationRequest.getOtp());

        AuthServiceImpl authServiceSpy = Mockito.spy(authServiceImpl);
        doThrow(new InvalidOTPException()).when(authServiceSpy).buildInvalidOTPException();
        assertThrows(InvalidOTPException.class, () -> authServiceSpy.otpVerification(otpVerificationRequest));

        verify(authRepoService).findOTPById(otpVerificationRequest.getChallengeId());
        verify(authRepoService, never()).saveAuthToken(any(AuthToken.class));
    }

    @Test
    void shouldPropagateException_whenOTPVerificationAuthRepoServiceFails() {
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(86400000L) // 1 day
            .userId(UUID.randomUUID())
            .build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(authRepoService).findOTPById(otpVerificationRequest.getChallengeId());

        Exception ex = assertThrows(RuntimeException.class, () -> authServiceImpl.otpVerification(otpVerificationRequest));
        assertSame(runtimeException, ex);

        verify(authRepoService).findOTPById(otpVerificationRequest.getChallengeId());
        verify(authRepoService, never()).saveAuthToken(any(AuthToken.class));
    }

    @Test
    void shouldReturnTrue_whenIsAllOk() {
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDE4NjUxMDksImV4cCI6MTc0MTk1MTUwOX0.das6YB90HEXhxzSOh8ukhHXmCjwPBmzHUx4yjIvaWJI")
            .userId(UUID.randomUUID())
            .build();
        boolean validation = true;

        doReturn(validation).when(jwtProvider).validateJwt(tokenValidationRequest.getAccessToken());
        assertTrue(validation);

        boolean result = authServiceImpl.tokenValidation(tokenValidationRequest);
        assertEquals(validation, result);

        verify(jwtProvider).validateJwt(tokenValidationRequest.getAccessToken());
    }

    @Test
    void shouldReturnFalse_whenTokenValidationFails() {
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDE4NjUxMDksImV4cCI6MTc0MTk1MTUwOX0.das6YB90HEXhxzSOh8ukhHXmCjwPBmzHUx4yjIvaWJI")
            .userId(UUID.randomUUID())
            .build();
        boolean validation = false;

        doReturn(validation).when(jwtProvider).validateJwt(tokenValidationRequest.getAccessToken());
        assertFalse(validation);

        boolean result = authServiceImpl.tokenValidation(tokenValidationRequest);
        assertEquals(validation, result);

        verify(jwtProvider).validateJwt(tokenValidationRequest.getAccessToken());
    }

    @Test
    void shouldPropagateException_whenTokenValidationFails() {
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDE4NjUxMDksImV4cCI6MTc0MTk1MTUwOX0.das6YB90HEXhxzSOh8ukhHXmCjwPBmzHUx4yjIvaWJI")
            .userId(UUID.randomUUID())
            .build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(jwtProvider).validateJwt(tokenValidationRequest.getAccessToken());

        Exception ex = assertThrows(RuntimeException.class, () -> authServiceImpl.tokenValidation(tokenValidationRequest));
        assertSame(runtimeException, ex);

        verify(jwtProvider).validateJwt(tokenValidationRequest.getAccessToken());
    }

    @Test
    void shouldReturnAuthToken_whenRefreshTokenIsAllOk() {
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
            .refreshTokenId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .build();
        AuthToken authToken = AuthToken.builder()
            .tokenId(refreshTokenRequest.getRefreshTokenId())
            .expiresIn(86400000L) // 1 day
            .message("Auth token generated")
            .userId(refreshTokenRequest.getUserId())
            .build();

        UUID uuid = UUID.randomUUID();
        doReturn(uuid).when(uuidProvider).generateUUID();

        AuthToken newAuthToken = AuthToken.builder()
            .tokenId(uuid)
            .expiresIn(86400000L) // 1 day
            .message("Auth token re-generated")
            .userId(refreshTokenRequest.getUserId())
            .build();

        doReturn(authToken).when(authRepoService).findAuthById(refreshTokenRequest.getRefreshTokenId());
        doNothing().when(authRepoService).saveAuthToken(newAuthToken);

        AuthToken authTokenResult = authServiceImpl.refreshToken(refreshTokenRequest);
        assertEquals(newAuthToken, authTokenResult);

        verify(authRepoService).findAuthById(refreshTokenRequest.getRefreshTokenId());
        verify(authRepoService).saveAuthToken(newAuthToken);
    }

    @Test
    void shouldPropagateException_whenRefreshTokenAuthRepoServiceFails() {
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
            .refreshTokenId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(authRepoService).findAuthById(refreshTokenRequest.getRefreshTokenId());

        Exception ex = assertThrows(RuntimeException.class, () -> authServiceImpl.refreshToken(refreshTokenRequest));
        assertSame(runtimeException, ex);

        verify(authRepoService).findAuthById(refreshTokenRequest.getRefreshTokenId());
    }

    @Test
    void shouldReturnUser_whenIsAllOk() {
        UUID userId = UUID.fromString("7f113bb2-38eb-47e7-84a2-cf2703004b86");
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDE4NjUxMDksImV4cCI6MTc0MTk1MTUwOX0.das6YB90HEXhxzSOh8ukhHXmCjwPBmzHUx4yjIvaWJI";
        doReturn(userId).when(jwtProvider).getUserIdFromJwt(accessToken);

        UserIdRequest userIDRequest = UserIdRequest.builder()
            .accessToken(accessToken)
            .build();
        User user = User.builder()
            .id(userId)
            .name("testName")
            .surname("testSurname")
            .username("testUser")
            .email("testEmail")
            .password("testPassword")
            .build();
        boolean validation = true;

        doReturn(validation).when(jwtProvider).validateJwt(userIDRequest.getAccessToken());
        assertTrue(validation);
        doReturn(user).when(usersRepoService).getUser(userId);

        User userResult = authServiceImpl.extractUUID(userIDRequest);
        assertEquals(user, userResult);

        verify(jwtProvider).validateJwt(userIDRequest.getAccessToken());
        verify(usersRepoService).getUser(userId);
    }

    @Test
    void shouldReturnNewInvalidOTPException_whenIsAllOk() {
        AuthServiceImpl authService = new AuthServiceImpl(null, null, null, null, null, null);

        InvalidOTPException invalidOTPException = authService.buildInvalidOTPException();
        assertNotNull(invalidOTPException);
        assertInstanceOf(InvalidOTPException.class, invalidOTPException);
    }

    @Test
    void shouldReturnNewOTPExpiredException_whenIsAllOk() {
        AuthServiceImpl authService = new AuthServiceImpl(null, null, null, null, null, null);

        OTPExpiredException otpExpiredException = authService.buildOTPExpiredException();
        assertNotNull(otpExpiredException);
        assertInstanceOf(OTPExpiredException.class, otpExpiredException);
    }
}