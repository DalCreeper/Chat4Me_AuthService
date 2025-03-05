package com.advancia.chat4me_auth_service.domain.repository;

import com.advancia.chat4me_auth_service.domain.model.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface AuthRepoService {
    Optional<User> findByUsernameAndPassword(String username, String encryptedPassword);
    Optional<User> findById(UUID id);
    User saveUser(User user);
    void saveOTPVerificationRequest(OTPVerificationRequest otpVerificationRequest);
    Optional<OTPVerificationRequest> findOTPById(UUID challengeId);
    void saveAuthToken(AuthToken authToken);
    Optional<AuthToken> findAuthById(UUID refreshTokenId);
}
