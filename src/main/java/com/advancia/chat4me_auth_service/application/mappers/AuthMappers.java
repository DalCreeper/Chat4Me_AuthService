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

    @Mapping(source = "tokenId", target = "tokenId")
    @Mapping(source = "accessToken", target = "accessToken")
    @Mapping(source = "expiresIn", target = "expiresIn")
    @Mapping(source = "userId", target = "userId")
    AuthTokenDto convertFromDomain(AuthToken authToken);
    @Mapping(source = "challengeId", target = "challengeId")
    @Mapping(source = "otp", target = "otp")
    @Mapping(source = "userId", target = "userId")
    OTPVerificationRequest convertToDomain(OTPVerificationRequestDto otpVerificationRequestDto);

    @Mapping(source = "tokenId", target = "tokenId")
    @Mapping(source = "accessToken", target = "accessToken")
    @Mapping(source = "userId", target = "userId")
    TokenValidationRequest convertToDomain(TokenValidationRequestDto tokenValidationRequestDto);

    @Mapping(source = "refreshTokenId", target = "refreshTokenId")
    @Mapping(source = "userId", target = "userId")
    RefreshTokenRequest convertToDomain(RefreshTokenRequestDto refreshTokenRequestDto);

    @Mapping(source = "accessToken", target = "accessToken")
    UserIdRequest convertToDomain(UserIdRequestDto userIdRequestDto);
}
