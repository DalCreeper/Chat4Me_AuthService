package com.advancia.chat4me_auth_service.domain.exceptions;

public class JWTNotValidatedException extends AuthServiceException {
    public JWTNotValidatedException() {
        super("JWT not validated");
    }

    public JWTNotValidatedException(Throwable throwable) {
        super("JWT not validated", throwable);
    }
}