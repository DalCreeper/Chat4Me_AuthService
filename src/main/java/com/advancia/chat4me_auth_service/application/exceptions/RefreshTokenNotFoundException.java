package com.advancia.chat4me_auth_service.application.exceptions;

public class RefreshTokenNotFoundException extends AuthServiceException {
    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }

    public RefreshTokenNotFoundException(Throwable throwable) {
        super("Refresh token not found", throwable);
    }
}