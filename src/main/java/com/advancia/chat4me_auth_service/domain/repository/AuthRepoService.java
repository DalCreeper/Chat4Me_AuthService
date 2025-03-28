package com.advancia.chat4me_auth_service.domain.repository;

import com.advancia.chat4me_auth_service.domain.model.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AuthRepoService {
    User findByUsernameAndPassword(String username, String encryptedPassword);
    User saveUser(User user);
    void saveOTPVerificationRequest(OTPVerificationRequest otpVerificationRequest);
    OTPVerificationRequest findOTPById(UUID challengeId);
    void saveAuthToken(AuthToken authToken);
    AuthToken findAuthById(UUID refreshTokenId);
}
