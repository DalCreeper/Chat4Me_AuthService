package com.advancia.chat4me_auth_service.application;

import com.advancia.Chat4Me_Auth_Service.generated.application.api.AuthApiDelegate;
import com.advancia.Chat4Me_Auth_Service.generated.application.model.ChallengeResponseDto;
import com.advancia.Chat4Me_Auth_Service.generated.application.model.LoginRequestDto;
import com.advancia.chat4me_auth_service.application.mappers.AuthMappers;
import com.advancia.chat4me_auth_service.domain.model.ChallengeResponse;
import com.advancia.chat4me_auth_service.domain.model.LoginRequest;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApiDelegateImpl implements AuthApiDelegate {

    private final AuthService authService;
    private final AuthMappers authMappers;

    @Override
    public ResponseEntity<ChallengeResponseDto> startLogin(LoginRequestDto loginRequestDto) {
        ChallengeResponse challengeResponse = authService.login(authMappers.convertToDomain(loginRequestDto));
        ChallengeResponseDto challengeResponseDto = authMappers.convertFromDomain(challengeResponse);

        return ResponseEntity.ok(challengeResponseDto);
    }

}
