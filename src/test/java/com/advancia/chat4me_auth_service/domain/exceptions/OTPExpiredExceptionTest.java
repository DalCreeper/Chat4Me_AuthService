package com.advancia.chat4me_auth_service.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OTPExpiredExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        OTPExpiredException otpExpiredException = new OTPExpiredException();

        assertEquals("OTP expired", otpExpiredException.getMessage());
        assertNull(otpExpiredException.getCause());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        Throwable cause = new RuntimeException("Underlying OTP expiration issue");
        OTPExpiredException otpExpiredException = new OTPExpiredException(cause);

        assertEquals("OTP expired", otpExpiredException.getMessage());
        assertEquals(cause, otpExpiredException.getCause());
    }
}