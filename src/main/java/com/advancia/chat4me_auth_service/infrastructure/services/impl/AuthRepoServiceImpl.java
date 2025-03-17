package com.advancia.chat4me_auth_service.infrastructure.services.impl;

import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.infrastructure.mappers.AuthEntityMappers;
import com.advancia.chat4me_auth_service.infrastructure.mappers.UserEntityMappers;
import com.advancia.chat4me_auth_service.infrastructure.model.AuthTokenEntity;
import com.advancia.chat4me_auth_service.infrastructure.model.OTPVerificationRequestEntity;
import com.advancia.chat4me_auth_service.infrastructure.model.UserEntity;
import com.advancia.chat4me_auth_service.infrastructure.repository.AuthTokenRepository;
import com.advancia.chat4me_auth_service.infrastructure.repository.OtpVerificationRepository;
import com.advancia.chat4me_auth_service.infrastructure.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class AuthRepoServiceImpl implements AuthRepoService {
    private final UsersRepository usersRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final AuthTokenRepository authTokenRepository;
    private final AuthEntityMappers authEntityMappers;
    private final UserEntityMappers userEntityMappers;

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String encryptedPassword) {
        Optional<UserEntity> userEntity = usersRepository.findByUsername(username).filter(user -> user.checkPassword(encryptedPassword));
        return userEntity.map(userEntityMappers::convertFromInfrastructure);
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserEntity> userEntity = usersRepository.findById(id);
        return userEntity.map(userEntityMappers::convertFromInfrastructure);
    }

    @Override
    public User saveUser(User user) {
        UserEntity savedUserEntity = userEntityMappers.convertToInfrastructure(user);
        UserEntity userEntity = usersRepository.save(savedUserEntity);
        return userEntityMappers.convertFromInfrastructure(userEntity);
    }

    @Override
    public void saveOTPVerificationRequest(OTPVerificationRequest otpVerificationRequest) {
        OTPVerificationRequestEntity otpVerificationRequestEntity = authEntityMappers.convertToInfrastructure(otpVerificationRequest);
        otpVerificationRepository.save(otpVerificationRequestEntity);
    }

    @Override
    public Optional<OTPVerificationRequest> findOTPById(UUID challengeId) {
        Optional<OTPVerificationRequestEntity> otpVerificationRequestEntity = otpVerificationRepository.findById(challengeId);
        return otpVerificationRequestEntity.map(authEntityMappers::convertFromInfrastructure);
    }

    @Override
    public void saveAuthToken(AuthToken authToken) {
        AuthTokenEntity authTokenEntity = authEntityMappers.convertToInfrastructure(authToken);
        authTokenRepository.save(authTokenEntity);
    }

    @Override
    public Optional<AuthToken> findAuthById(UUID refreshTokenId) {
        Optional<AuthTokenEntity> authTokenEntity = authTokenRepository.findById(refreshTokenId);
        return authTokenEntity.map(authEntityMappers::convertFromInfrastructure);
    }
}