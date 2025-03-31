package com.advancia.chat4me_auth_service.application.exceptions;

public class InvalidCredentialException extends AuthServiceException {
    public InvalidCredentialException() {
        super("Invalid credentials");
    }

    public InvalidCredentialException(Throwable throwable) {
        super("Invalid credentials", throwable);
    }
}