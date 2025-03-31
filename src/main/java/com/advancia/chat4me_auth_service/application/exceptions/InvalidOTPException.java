package com.advancia.chat4me_auth_service.application.exceptions;

public class InvalidOTPException extends AuthServiceException {
    public InvalidOTPException() {
        super("Invalid OTP");
    }

    public InvalidOTPException(Throwable throwable) {
        super("Invalid OTP", throwable);
    }
}