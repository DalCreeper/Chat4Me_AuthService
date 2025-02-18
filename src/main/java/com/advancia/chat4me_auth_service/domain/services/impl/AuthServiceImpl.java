package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.*;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    //private final AuthRepoService authRepoService;  // TODO : DA IMPLEMENTARE AL POSTO DEI DATI LOCALI

    @Override
    public ChallengeResponse login(LoginRequest loginRequest) {
        LoginRequest localTempLoginRequest = LoginRequest.builder() // TODO : VARIABILE LOCALE
            .username("Prova")
            .password("Prova123!")
            .build();

        // TODO : QUA VERRA' INSERITO IL CONTROLLO DELLO USERNAME E PASSWORD, SE CORRISPONDONO SU DB VERRA' RESTITUITO LO UUID
        String otp = "123456";
        System.out.println("OTP: " + otp + "\nFor user: " + loginRequest.getUsername());

        if(loginRequest.getUsername().equals(localTempLoginRequest.getUsername()) && loginRequest.getPassword().equals(localTempLoginRequest.getPassword())) {
            return ChallengeResponse.builder()
                .challengeId("0001")
                .message("Correct data")
                .userId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .build();
        } else {
            return ChallengeResponse.builder().build();
        }
    }

    @Override
    public AuthToken otpVerification(OTPVerificationRequest otpVerificationRequest) {
        OTPVerificationRequest localTempOTPVerificationRequest = OTPVerificationRequest.builder() // TODO : VARIABILE LOCALE
            .challengeId("0001")
            .otp("123456")
            .userId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))
            .build();

        if(otpVerificationRequest.getOtp().equals(localTempOTPVerificationRequest.getOtp())){
            return AuthToken.builder()
                .tokenId(1)
                .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2V4YW1wbGUuYXV0aDAuY29tLyIsImF1ZCI6Imh0dHBzOi8vYXBpLmV4YW1wbGUuY29tL2NhbGFuZGFyL3YxLyIsInN1YiI6InVzcl8xMjMiLCJpYXQiOjE0NTg3ODU3OTYsImV4cCI6MTQ1ODg3MjE5Nn0.CA7eaHjIHz5NxeIJoFK9krqaeZrPLwmMmgI_XiQiIkQ")
                .expiresIn(100000)
                .build();
        } else {
            return AuthToken.builder().build();
        }
    }

    @Override
    public String tokenValidation(TokenValidationRequest tokenValidationRequest) {
        TokenValidationRequest localTempTokenValidationRequest = TokenValidationRequest.builder()   // TODO : VARIABILE LOCALE
            .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2V4YW1wbGUuYXV0aDAuY29tLyIsImF1ZCI6Imh0dHBzOi8vYXBpLmV4YW1wbGUuY29tL2NhbGFuZGFyL3YxLyIsInN1YiI6InVzcl8xMjMiLCJpYXQiOjE0NTg3ODU3OTYsImV4cCI6MTQ1ODg3MjE5Nn0.CA7eaHjIHz5NxeIJoFK9krqaeZrPLwmMmgI_XiQiIkQ")
            .userId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))
            .build();

        if(tokenValidationRequest.getAccessToken().equals(localTempTokenValidationRequest.getAccessToken())){
            return "Valid";
        } else {
            return "Invalid";
        }
    }

    @Override
    public AuthToken refreshToken(RefreshTokenRequest refreshTokenRequest) {
        if(refreshTokenRequest.getRefreshTokenId() == 1) {  // TODO : CONTROLLO LOCALE SOLO PER VERIFICARE IL FUNZIONAMENTO
            // TODO : QUA VERRA' INSERITA SU DB LA REFERENCE DELL'ID UTENTE CONGIUNTO AL NUOVO TOKENID
            return AuthToken.builder()
                .tokenId(2)
                .accessToken("aaJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2V4YW1wbGUuYXV0aDAuY29tLyIsImF1ZCI6Imh0dHBzOi8vYXBpLmV4YW1wbGUuY29tL2NhbGFuZGFyL3YxLyIsInN1YiI6InVzcl8xMjMiLCJpYXQiOjE0NTg3ODU3OTYsImV4cCI6MTQ1ODg3MjE5Nn0.CA7eaHjIHz5NxeIJoFK9krqaeZrPLwmMmgI_XiQiIkQ")
                .expiresIn(100000)
                .build();
        } else {
            return AuthToken.builder().build();
        }
    }
}
