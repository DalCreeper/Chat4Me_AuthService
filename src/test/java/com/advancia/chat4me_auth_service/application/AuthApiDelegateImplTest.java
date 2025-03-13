package com.advancia.chat4me_auth_service.application;

import com.advancia.Chat4Me_Auth_Service.generated.application.model.*;
import com.advancia.chat4me_auth_service.application.mappers.AuthMappers;
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
    void shouldReturnChallengeResponseDtoAndPrintOTP_whenAllOk() {
        LoginRequestDto loginRequestDto = new LoginRequestDto()
            .username("testUser")
            .password("testPassword");
        LoginRequest loginRequest = LoginRequest.builder().build();
        ChallengeResponse challengeResponse = ChallengeResponse.builder().build();
        ChallengeResponseDto challengeResponseDto = new ChallengeResponseDto();

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
        LoginRequest loginRequest = LoginRequest.builder().build();
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
    void shouldPropagateException_whenLoginAuthMappersFails() {
        LoginRequestDto loginRequestDto = new LoginRequestDto()
            .username("testUser")
            .password("testPassword");
        RuntimeException runtimeException = new RuntimeException("Mapping error");

        doThrow(runtimeException).when(authMappers).convertToDomain(loginRequestDto);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.startLogin(loginRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(loginRequestDto);
        verify(authService, never()).login(any(LoginRequest.class));
        verify(authMappers, never()).convertFromDomain(any(ChallengeResponse.class));
    }

    @Test
    void shouldReturnAuthTokenDtoAndPrintToken_whenAllOk() {
        OTPVerificationRequestDto otpVerificationRequestDto = new OTPVerificationRequestDto()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(1740478333L)
            .userId(UUID.randomUUID());
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder().build();
        AuthToken authToken = AuthToken.builder().build();
        AuthTokenDto authTokenDto = new AuthTokenDto();

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
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder().build();
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
    void shouldPropagateException_whenOTPAuthMappersFails() {
        OTPVerificationRequestDto otpVerificationRequestDto = new OTPVerificationRequestDto()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(1740478333L)
            .userId(UUID.randomUUID());
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(authMappers).convertToDomain(otpVerificationRequestDto);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.verifyOTP(otpVerificationRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(otpVerificationRequestDto);
        verify(authService, never()).otpVerification(any(OTPVerificationRequest.class));
        verify(authMappers, never()).convertFromDomain(any(ChallengeResponse.class));
    }

    @Test
    void shouldValidateAndReturnAnEmptyBody_whenAllOk() {
        TokenValidationRequestDto tokenValidationRequestDto = new TokenValidationRequestDto()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .userId(UUID.randomUUID());
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder().build();

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
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder().build();

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
        TokenValidationRequest tokenValidationRequest = TokenValidationRequest.builder().build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doReturn(tokenValidationRequest).when(authMappers).convertToDomain(tokenValidationRequestDto);
        doThrow(runtimeException).when(authService).tokenValidation(tokenValidationRequest);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.validateToken(tokenValidationRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(tokenValidationRequestDto);
        verify(authService).tokenValidation(tokenValidationRequest);
    }

    @Test
    void shouldPropagateException_whenTokenValidationAuthMappersFails() {
        TokenValidationRequestDto tokenValidationRequestDto = new TokenValidationRequestDto()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .userId(UUID.randomUUID());
        RuntimeException runtimeException = new RuntimeException("Mapping error");

        doThrow(runtimeException).when(authMappers).convertToDomain(tokenValidationRequestDto);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.validateToken(tokenValidationRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(tokenValidationRequestDto);
        verify(authService, never()).tokenValidation(any(TokenValidationRequest.class));
    }

    @Test
    void shouldReturnARefreshAuthTokenDtoAndPrintToken_whenAllOk() {
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto()
            .refreshTokenId(UUID.randomUUID())
            .userId(UUID.randomUUID());
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder().build();
        AuthToken authToken = AuthToken.builder().build();
        AuthTokenDto authTokenDto = new AuthTokenDto();

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
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder().build();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doReturn(refreshTokenRequest).when(authMappers).convertToDomain(refreshTokenRequestDto);
        doThrow(runtimeException).when(authService).refreshToken(refreshTokenRequest);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.refreshToken(refreshTokenRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(refreshTokenRequestDto);
        verify(authService).refreshToken(refreshTokenRequest);
        verify(authMappers, never()).convertFromDomain(any(AuthToken.class));
    }

    @Test
    void shouldPropagateException_whenRefreshAuthTokenAuthMappersFails() {
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto()
            .refreshTokenId(UUID.randomUUID())
            .userId(UUID.randomUUID());
        RuntimeException runtimeException = new RuntimeException("Mapping error");

        doThrow(runtimeException).when(authMappers).convertToDomain(refreshTokenRequestDto);

        Exception ex = assertThrows(RuntimeException.class, () -> authApiDelegateImpl.refreshToken(refreshTokenRequestDto));
        assertSame(runtimeException, ex);

        verify(authMappers).convertToDomain(refreshTokenRequestDto);
        verify(authService, never()).refreshToken(any(RefreshTokenRequest.class));
        verify(authMappers, never()).convertFromDomain(any(AuthToken.class));
    }
}