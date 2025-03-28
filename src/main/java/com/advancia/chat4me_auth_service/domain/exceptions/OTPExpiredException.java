package com.advancia.chat4me_auth_service.domain.exceptions;

public class OTPExpiredException extends AuthServiceException {
    public OTPExpiredException() {
        super("OTP expired");
    }

    public OTPExpiredException(Throwable throwable) {
        super("OTP expired", throwable);
    }
}
