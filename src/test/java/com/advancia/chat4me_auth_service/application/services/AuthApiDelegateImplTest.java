package com.advancia.chat4me_auth_service.application.services;

import com.advancia.Chat4Me_Auth_Service.generated.application.model.*;
import com.advancia.chat4me_auth_service.application.mappers.AuthMappers;
import com.advancia.chat4me_auth_service.application.services.impl.AuthApiDelegateImpl;
import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthApiDelegateImplTest {
    @Mock
    private AuthService authService;
    @Mock
    private AuthMappers authMappers;
    @InjectMocks
    private AuthApiDelegateImpl authApiDelegateImpl;

    @Test
    void shouldReturnChallengeResponseDtoAndPrintOTP_whenIsAllOk() {
        LoginRequestDto loginRequestDto = new LoginRequestDto()
            .username("testUser")
            .password("testPassword");
        LoginRequest loginRequest = LoginRequest.builder()
            .username(loginRequestDto.getUsername())
            .password(loginRequestDto.getPassword())
            .build();
        ChallengeResponse challengeResponse = ChallengeResponse.builder()
            .challengeId(UUID.randomUUID())
            .message("test")
            .userId(UUID.randomUUID())
            .build();
        ChallengeResponseDto challengeResponseDto = new ChallengeResponseDto()
            .challengeId(challengeResponse.getChallengeId())
            .message(challengeResponse.getMessage())
            .userId(challengeResponse.getUserId());

        doReturn(loginRequest).when(authMappers).convertToDomain(loginRequestDto);
        doReturn(challengeResponse).when(authService).login(loginRequest);
        doReturn(challengeResponseDto).when(authMappers).convertFromDomain(challengeResponse);

        ResponseEntity<ChallengeResponseDto> response = authApiDelegateImpl.startLogin(loginRequestDto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(challengeResponseDto, response.getBody());

        verify(authMappers).convertToDomain(loginRequestDto);
        verify(authService).login(loginRequest);
        verify(authMappers).convertFromDomain(challengeResponse);
    }

    @Test
    void shouldPropagateException_whenLoginAuthServiceFails() {
        LoginRequestDto loginRequestDto = new LoginRequestDto()
            .username("testUser")
            .password("testPassword");
        LoginRequest loginRequest = LoginRequest.builder()
            .username(loginRequestDto.getUsername())
            .password(loginRequestDto.getPassword())
            .build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doReturn(loginRequest).when(authMappers).convertToDomain(loginRequestDto);
        doThrow(runtimeException).when(authService).login(loginRequest);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.startLogin(loginRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(loginRequestDto);
        verify(authService).login(loginRequest);
        verify(authMappers, never()).convertFromDomain(any(ChallengeResponse.class));
    }

    @Test
    void shouldReturnAuthTokenDtoAndPrintToken_whenIsAllOk() {
        OTPVerificationRequestDto otpVerificationRequestDto = new OTPVerificationRequestDto()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(1740478333L)
            .userId(UUID.randomUUID());
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(otpVerificationRequestDto.getChallengeId())
            .otp(otpVerificationRequestDto.getOtp())
            .expiresAt(otpVerificationRequestDto.getExpiresAt())
            .userId(otpVerificationRequestDto.getUserId())
            .build();
        AuthToken authToken = AuthToken.builder()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .expiresIn(86400000L)
            .message("Auth token generated")
            .userId(otpVerificationRequest.getUserId())
            .build();
        AuthTokenDto authTokenDto = new AuthTokenDto()
            .tokenId(authToken.getTokenId())
            .accessToken(authToken.getAccessToken())
            .expiresIn(authToken.getExpiresIn())
            .message(authToken.getMessage())
            .userId(otpVerificationRequest.getUserId());

        doReturn(otpVerificationRequest).when(authMappers).convertToDomain(otpVerificationRequestDto);
        doReturn(authToken).when(authService).otpVerification(otpVerificationRequest);
        doReturn(authTokenDto).when(authMappers).convertFromDomain(authToken);

        ResponseEntity<AuthTokenDto> response = authApiDelegateImpl.verifyOTP(otpVerificationRequestDto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(authTokenDto, response.getBody());

        verify(authMappers).convertToDomain(otpVerificationRequestDto);
        verify(authService).otpVerification(otpVerificationRequest);
        verify(authMappers).convertFromDomain(authToken);
    }

    @Test
    void shouldPropagateException_whenOTPAuthServiceFails() {
        OTPVerificationRequestDto otpVerificationRequestDto = new OTPVerificationRequestDto()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(1740478333L)
            .userId(UUID.randomUUID());
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(otpVerificationRequestDto.getChallengeId())
            .otp(otpVerificationRequestDto.getOtp())
            .expiresAt(otpVerificationRequestDto.getExpiresAt())
            .userId(otpVerificationRequestDto.getUserId())
            .build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doReturn(otpVerificationRequest).when(authMappers).convertToDomain(otpVerificationRequestDto);
        doThrow(runtimeException).when(authService).otpVerification(otpVerificationRequest);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.verifyOTP(otpVerificationRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(otpVerificationRequestDto);
        verify(authService).otpVerification(otpVerificationRequest);
        verify(authMappers, never()).convertFromDomain(any(ChallengeResponse.class));
    }

    @Test
    void shouldValidateAndReturnAnEmptyBody_whenIsAllOk() {
        TokenValidationRequestDto tokenValidationRequestDto = new TokenValidationRequestDto()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .userId(UUID.randomUUID());
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder()
            .tokenId(tokenValidationRequestDto.getTokenId())
            .accessToken(tokenValidationRequestDto.getAccessToken())
            .userId(tokenValidationRequestDto.getUserId())
            .build();

        doReturn(tokenValidationRequest).when(authMappers).convertToDomain(tokenValidationRequestDto);
        doReturn(true).when(authService).tokenValidation(tokenValidationRequest);

        ResponseEntity<Void> response = authApiDelegateImpl.validateToken(tokenValidationRequestDto);
        assertEquals(200, response.getStatusCode().value());

        verify(authMappers).convertToDomain(tokenValidationRequestDto);
        verify(authService).tokenValidation(tokenValidationRequest);
    }

    @Test
    void shouldReturnBadRequest_whenTokenValidationFails() {
        TokenValidationRequestDto tokenValidationRequestDto = new TokenValidationRequestDto()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .userId(UUID.randomUUID());
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder()
            .tokenId(tokenValidationRequestDto.getTokenId())
            .accessToken(tokenValidationRequestDto.getAccessToken())
            .userId(tokenValidationRequestDto.getUserId())
            .build();

        doReturn(tokenValidationRequest).when(authMappers).convertToDomain(tokenValidationRequestDto);
        doReturn(false).when(authService).tokenValidation(tokenValidationRequest);

        ResponseEntity<Void> response = authApiDelegateImpl.validateToken(tokenValidationRequestDto);
        assertEquals(400, response.getStatusCode().value());

        verify(authMappers).convertToDomain(tokenValidationRequestDto);
        verify(authService).tokenValidation(tokenValidationRequest);
    }

    @Test
    void shouldPropagateException_whenTokenValidationAuthServiceFails() {
        TokenValidationRequestDto tokenValidationRequestDto = new TokenValidationRequestDto()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .userId(UUID.randomUUID());
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder()
            .tokenId(tokenValidationRequestDto.getTokenId())
            .accessToken(tokenValidationRequestDto.getAccessToken())
            .userId(tokenValidationRequestDto.getUserId())
            .build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doReturn(tokenValidationRequest).when(authMappers).convertToDomain(tokenValidationRequestDto);
        doThrow(runtimeException).when(authService).tokenValidation(tokenValidationRequest);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.validateToken(tokenValidationRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(tokenValidationRequestDto);
        verify(authService).tokenValidation(tokenValidationRequest);
    }

    @Test
    void shouldReturnARefreshAuthTokenDtoAndPrintToken_whenIsAllOk() {
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto()
            .refreshTokenId(UUID.randomUUID())
            .userId(UUID.randomUUID());
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
            .refreshTokenId(refreshTokenRequestDto.getRefreshTokenId())
            .userId(refreshTokenRequestDto.getUserId())
            .build();
        AuthToken authToken = AuthToken.builder()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .expiresIn(86400000L) // 1 day
            .message("Auth token generated")
            .userId(refreshTokenRequest.getUserId())
            .build();
        AuthTokenDto authTokenDto = new AuthTokenDto()
            .tokenId(authToken.getTokenId())
            .accessToken(authToken.getAccessToken())
            .userId(authToken.getUserId())
            .expiresIn(authToken.getExpiresIn())
            .message(authToken.getMessage())
            .userId(authToken.getUserId());

        doReturn(refreshTokenRequest).when(authMappers).convertToDomain(refreshTokenRequestDto);
        doReturn(authToken).when(authService).refreshToken(refreshTokenRequest);
        doReturn(authTokenDto).when(authMappers).convertFromDomain(authToken);

        ResponseEntity<AuthTokenDto> response = authApiDelegateImpl.refreshToken(refreshTokenRequestDto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(authTokenDto, response.getBody());

        verify(authMappers).convertToDomain(refreshTokenRequestDto);
        verify(authService).refreshToken(refreshTokenRequest);
        verify(authMappers).convertFromDomain(authToken);
    }

    @Test
    void shouldPropagateException_whenRefreshAuthTokenFails() {
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto()
            .refreshTokenId(UUID.randomUUID())
            .userId(UUID.randomUUID());
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
            .refreshTokenId(refreshTokenRequestDto.getRefreshTokenId())
            .userId(refreshTokenRequestDto.getUserId())
            .build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doReturn(refreshTokenRequest).when(authMappers).convertToDomain(refreshTokenRequestDto);
        doThrow(runtimeException).when(authService).refreshToken(refreshTokenRequest);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.refreshToken(refreshTokenRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(refreshTokenRequestDto);
        verify(authService).refreshToken(refreshTokenRequest);
        verify(authMappers, never()).convertFromDomain(any(AuthToken.class));
    }
}