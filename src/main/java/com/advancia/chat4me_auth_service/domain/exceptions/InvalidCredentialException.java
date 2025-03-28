package com.advancia.chat4me_auth_service.domain.exceptions;

public class InvalidCredentialException extends AuthServiceException {
    public InvalidCredentialException() {
        super("Invalid credentials");
    }

    public InvalidCredentialException(Throwable throwable) {
        super("Invalid credentials", throwable);
    }
}