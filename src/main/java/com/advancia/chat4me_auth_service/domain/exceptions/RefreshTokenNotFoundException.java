package com.advancia.chat4me_auth_service.domain.exceptions;

public class RefreshTokenNotFoundException extends AuthServiceException {
    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }

    public RefreshTokenNotFoundException(Throwable throwable) {
        super("Refresh token not found", throwable);
    }
}