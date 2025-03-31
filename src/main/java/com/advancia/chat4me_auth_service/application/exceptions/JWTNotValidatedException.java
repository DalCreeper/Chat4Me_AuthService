package com.advancia.chat4me_auth_service.application.exceptions;

public class JWTNotValidatedException extends AuthServiceException {
    public JWTNotValidatedException() {
        super("JWT not validated");
    }

    public JWTNotValidatedException(Throwable throwable) {
        super("JWT not validated", throwable);
    }

    public JWTNotValidatedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}