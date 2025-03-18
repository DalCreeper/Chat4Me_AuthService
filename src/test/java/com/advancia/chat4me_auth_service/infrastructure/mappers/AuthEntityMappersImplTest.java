package com.advancia.chat4me_auth_service.infrastructure.mappers;

import com.advancia.chat4me_auth_service.domain.model.AuthToken;
import com.advancia.chat4me_auth_service.domain.model.OTPVerificationRequest;
import com.advancia.chat4me_auth_service.infrastructure.model.AuthTokenEntity;
import com.advancia.chat4me_auth_service.infrastructure.model.OTPVerificationRequestEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthEntityMappersImplTest {
    @InjectMocks
    private AuthEntityMappersImpl authEntityMappersImpl;

    @Test
    void shouldConvertAuthTokenFromInfrastructure_whenIsAllOk() {
        AuthTokenEntity authTokenEntity = AuthTokenEntity.builder()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .expiresIn(86400000L) // 1 day
            .message("Auth token generated")
            .userId(UUID.randomUUID())
            .build();

        AuthToken authToken = authEntityMappersImpl.convertFromInfrastructure(authTokenEntity);
        assertNotNull(authToken);
        assertEquals(authTokenEntity.getTokenId(), authToken.getTokenId());
        assertEquals(authTokenEntity.getAccessToken(), authToken.getAccessToken());
        assertEquals(authTokenEntity.getExpiresIn(), authToken.getExpiresIn());
        assertEquals(authTokenEntity.getUserId(), authToken.getUserId());
        assertEquals(authTokenEntity.getMessage(), authToken.getMessage());
    }

    @Test
    void shouldReturnNull_whenAuthTokenEntityIsNull() {
        assertNull(authEntityMappersImpl.convertFromInfrastructure((AuthTokenEntity) null));
    }

    @Test
    void shouldConvertAuthTokenToInfrastructure_whenIsAllOk() {
        AuthToken authToken = AuthToken.builder()
            .tokenId(UUID.randomUUID())
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .expiresIn(86400000L) // 1 day
            .message("Auth token generated")
            .userId(UUID.randomUUID())
            .build();

        AuthTokenEntity authTokenEntity = authEntityMappersImpl.convertToInfrastructure(authToken);
        assertNotNull(authTokenEntity);
        assertEquals(authToken.getTokenId(), authTokenEntity.getTokenId());
        assertEquals(authToken.getAccessToken(), authTokenEntity.getAccessToken());
        assertEquals(authToken.getExpiresIn(), authTokenEntity.getExpiresIn());
        assertEquals(authToken.getUserId(), authTokenEntity.getUserId());
        assertEquals(authToken.getMessage(), authTokenEntity.getMessage());
    }

    @Test
    void shouldReturnNull_whenAuthTokenIsNull() {
        assertNull(authEntityMappersImpl.convertToInfrastructure((AuthToken) null));
    }

    @Test
    void shouldConvertOTPVerificationRequestFromInfrastructure_whenIsAllOk() {
        OTPVerificationRequestEntity otpVerificationRequestEntity = OTPVerificationRequestEntity.builder()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(1740478333L)
            .userId(UUID.randomUUID())
            .build();

        OTPVerificationRequest otpVerificationRequest = authEntityMappersImpl.convertFromInfrastructure(otpVerificationRequestEntity);
        assertNotNull(otpVerificationRequest);
        assertEquals(otpVerificationRequestEntity.getChallengeId(), otpVerificationRequest.getChallengeId());
        assertEquals(otpVerificationRequestEntity.getOtp(), otpVerificationRequest.getOtp());
        assertEquals(otpVerificationRequestEntity.getUserId(), otpVerificationRequest.getUserId());
        assertEquals(otpVerificationRequestEntity.getExpiresAt(), otpVerificationRequest.getExpiresAt());
    }

    @Test
    void shouldReturnNull_whenOTPVerificationRequestEntityIsNull() {
        assertNull(authEntityMappersImpl.convertFromInfrastructure((OTPVerificationRequestEntity) null));
    }

    @Test
    void shouldConvertOTPVerificationRequestToInfrastructure_whenIsAllOk() {
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(1740478333L)
            .userId(UUID.randomUUID())
            .build();

        OTPVerificationRequestEntity otpVerificationRequestEntity = authEntityMappersImpl.convertToInfrastructure(otpVerificationRequest);
        assertNotNull(otpVerificationRequestEntity);
        assertEquals(otpVerificationRequest.getChallengeId(), otpVerificationRequestEntity.getChallengeId());
        assertEquals(otpVerificationRequest.getOtp(), otpVerificationRequestEntity.getOtp());
        assertEquals(otpVerificationRequest.getUserId(), otpVerificationRequestEntity.getUserId());
        assertEquals(otpVerificationRequest.getExpiresAt(), otpVerificationRequestEntity.getExpiresAt());
    }

    @Test
    void shouldReturnNull_whenOTPVerificationRequestIsNull() {
        assertNull(authEntityMappersImpl.convertToInfrastructure((OTPVerificationRequest) null));
    }
}