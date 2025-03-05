package com.advancia.chat4me_auth_service.infrastructure.mappers;

import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.infrastructure.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthEntityMappers {
    @Mapping(source = "tokenId", target = "tokenId")
    @Mapping(source = "accessToken", target = "accessToken")
    @Mapping(source = "expiresIn", target = "expiresIn")
    @Mapping(source = "userId", target = "userId")
    AuthToken convertFromInfrastructure(AuthTokenEntity authTokenEntity);
    @Mapping(source = "tokenId", target = "tokenId")
    @Mapping(source = "accessToken", target = "accessToken")
    @Mapping(source = "expiresIn", target = "expiresIn")
    @Mapping(source = "userId", target = "userId")
    AuthTokenEntity convertToInfrastructure(AuthToken authToken);

    @Mapping(source = "challengeId", target = "challengeId")
    @Mapping(source = "otp", target = "otp")
    @Mapping(source = "userId", target = "userId")
    OTPVerificationRequest convertFromInfrastructure(OTPVerificationRequestEntity otpVerificationRequestEntity);
    @Mapping(source = "challengeId", target = "challengeId")
    @Mapping(source = "otp", target = "otp")
    @Mapping(source = "userId", target = "userId")
    OTPVerificationRequestEntity convertToInfrastructure(OTPVerificationRequest otpVerificationRequest);

    @Mapping(source = "refreshTokenId", target = "refreshTokenId")
    @Mapping(source = "userId", target = "userId")
    RefreshTokenRequestEntity convertToInfrastructure(RefreshTokenRequest refreshTokenRequest);
}