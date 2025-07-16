package com.advancia.chat4me_auth_service.application.mappers;

import com.advancia.chat4me_auth_service.generated.application.model.*;
import com.advancia.chat4me_auth_service.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthMappersImplTest {
    @InjectMocks
    private AuthMappersImpl authMappersImpl;

    @Test
    void shouldConvertChallengeResponseFromDomain_whenIsAllOk() {
        ChallengeResponse challengeResponse = ChallengeResponse.builder()
            .challengeId(UUID.randomUUID())
            .message("test")
            .userId(UUID.randomUUID())
            .build();

        ChallengeResponseDto challengeResponseDto = authMappersImpl.convertFromDomain(challengeResponse);
        assertNotNull(challengeResponseDto);
        assertEquals(challengeResponse.getChallengeId(), challengeResponseDto.getChallengeId());
        assertEquals(challengeResponse.getMessage(), challengeResponseDto.getMessage());
        assertEquals(challengeResponse.getUserId(), challengeResponseDto.getUserId());
    }

    @Test
    void shouldReturnNull_whenChallengeResponseIsNull() {
        assertNull(authMappersImpl.convertFromDomain((ChallengeResponse) null));
    }

    @Test
    void shouldConvertLoginRequestToDomain_whenIsAllOk() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(
            "username",
            "password"
        );

        LoginRequest loginRequest = authMappersImpl.convertToDomain(loginRequestDto);
        assertNotNull(loginRequest);
        assertEquals(loginRequestDto.getUsername(), loginRequest.getUsername());
        assertEquals(loginRequestDto.getPassword(), loginRequest.getPassword());
    }

    @Test
    void shouldReturnNull_whenLoginRequestDtoIsNull() {
        assertNull(authMappersImpl.convertToDomain((LoginRequestDto) null));
    }

    @Test
    void shouldConvertAuthTokenFromDomain_whenIsAllOk() {
        AuthToken authToken = AuthToken.builder()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .expiresIn(86400000L)   // 1 day
            .message("Auth token generated")
            .userId(UUID.randomUUID())
            .build();

        AuthTokenDto authTokenDto = authMappersImpl.convertFromDomain(authToken);
        assertNotNull(authTokenDto);
        assertEquals(authToken.getTokenId(), authTokenDto.getTokenId());
        assertEquals(authToken.getAccessToken(), authTokenDto.getAccessToken());
        assertEquals(authToken.getExpiresIn(), authTokenDto.getExpiresIn());
        assertEquals(authToken.getUserId(), authTokenDto.getUserId());
        assertEquals(authToken.getMessage(), authTokenDto.getMessage());
    }

    @Test
    void shouldReturnNull_whenAuthTokenIsNull() {
        assertNull(authMappersImpl.convertFromDomain((AuthToken) null));
    }

    @Test
    void shouldConvertOTPVerificationRequestToDomain_whenIsAllOk() {
        OTPVerificationRequestDto otpVerificationRequestDto = new OTPVerificationRequestDto()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(1740478333L)
            .userId(UUID.randomUUID());

        OTPVerificationRequest otpVerificationRequest = authMappersImpl.convertToDomain(otpVerificationRequestDto);
        assertNotNull(otpVerificationRequest);
        assertEquals(otpVerificationRequestDto.getChallengeId(), otpVerificationRequest.getChallengeId());
        assertEquals(otpVerificationRequestDto.getOtp(), otpVerificationRequest.getOtp());
        assertEquals(otpVerificationRequestDto.getUserId(), otpVerificationRequest.getUserId());
        assertEquals(otpVerificationRequestDto.getExpiresAt(), otpVerificationRequest.getExpiresAt());
    }

    @Test
    void shouldReturnNull_whenOTPVerificationRequestDtoIsNull() {
        assertNull(authMappersImpl.convertToDomain((OTPVerificationRequestDto) null));
    }

    @Test
    void shouldConvertTokenValidationRequestToDomain_whenIsAllOk() {
        TokenValidationRequestDto tokenValidationRequestDto = new TokenValidationRequestDto()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .userId(UUID.randomUUID());

        TokenValidationRequest tokenValidationRequest = authMappersImpl.convertToDomain(tokenValidationRequestDto);
        assertNotNull(tokenValidationRequest);
        assertEquals(tokenValidationRequestDto.getTokenId(), tokenValidationRequest.getTokenId());
        assertEquals(tokenValidationRequestDto.getAccessToken(), tokenValidationRequest.getAccessToken());
        assertEquals(tokenValidationRequestDto.getUserId(), tokenValidationRequest.getUserId());
    }

    @Test
    void shouldReturnNull_whenTokenValidationRequestDtoIsNull() {
        assertNull(authMappersImpl.convertToDomain((TokenValidationRequestDto) null));
    }

    @Test
    void shouldConvertRefreshTokenRequestToDomain_whenIsAllOk() {
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto()
            .refreshTokenId(UUID.randomUUID())
            .userId(UUID.randomUUID());

        RefreshTokenRequest refreshTokenRequest = authMappersImpl.convertToDomain(refreshTokenRequestDto);
        assertNotNull(refreshTokenRequest);
        assertEquals(refreshTokenRequestDto.getRefreshTokenId(), refreshTokenRequest.getRefreshTokenId());
        assertEquals(refreshTokenRequestDto.getUserId(), refreshTokenRequest.getUserId());
    }

    @Test
    void shouldReturnNull_whenRefreshTokenRequestDtoIsNull() {
        assertNull(authMappersImpl.convertToDomain((RefreshTokenRequestDto) null));
    }

    @Test
    void shouldConvertUserIDRequestToDomain_whenIsAllOk() {
        UserIdRequestDto userIdRequestDto = new UserIdRequestDto()
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik");

        UserIdRequest userIdRequest = authMappersImpl.convertToDomain(userIdRequestDto);
        assertNotNull(userIdRequest);
        assertEquals(userIdRequestDto.getAccessToken(), userIdRequest.getAccessToken());
    }

    @Test
    void shouldReturnNull_whenUserIDRequestDtoIsNull() {
        assertNull(authMappersImpl.convertToDomain((UserIdRequestDto) null));
    }
}