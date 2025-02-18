package com.advancia.chat4me_auth_service.application.mappers;

import com.advancia.Chat4Me_Auth_Service.generated.application.model.*;
import com.advancia.chat4me_auth_service.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthMappers {
    @Mapping(source = "challengeId", target = "challengeId")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "userId", target = "userId")
    ChallengeResponseDto convertFromDomain(ChallengeResponse challengeResponse);
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    LoginRequest convertToDomain(LoginRequestDto loginRequestDto);

    @Mapping(source = "challengeId", target = "challengeId")
    @Mapping(source = "otp", target = "otp")
    @Mapping(source = "userId", target = "userId")
    OTPVerificationRequestDto convertFromDomain(OTPVerificationRequest otpVerificationRequest);

    @Mapping(source = "refreshTokenId", target = "refreshTokenId")
    @Mapping(source = "userId", target = "userId")
    RefreshTokenRequestDto convertFromDomain(RefreshTokenRequest refreshTokenRequest);

    @Mapping(source = "accessToken", target = "accessToken")
    @Mapping(source = "userId", target = "userId")
    TokenValidationRequestDto convertFromDomain(TokenValidationRequest tokenValidationRequest);
}
