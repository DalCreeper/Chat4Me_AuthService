package com.advancia.chat4me_auth_service.domain.exceptions;

public class UserNotFoundException extends AuthServiceException {
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(Throwable throwable) {
        super("User not found", throwable);
    }
}