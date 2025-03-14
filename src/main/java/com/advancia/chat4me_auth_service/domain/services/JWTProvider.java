package com.advancia.chat4me_auth_service.domain.services;

import java.util.UUID;

public interface JWTProvider {
    String generateJwt(UUID userId);
    boolean validateJwt(String token);
}
