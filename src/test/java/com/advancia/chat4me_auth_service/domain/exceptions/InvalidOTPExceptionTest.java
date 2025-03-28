package com.advancia.chat4me_auth_service.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InvalidOTPExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        InvalidOTPException invalidOTPException = new InvalidOTPException();

        assertEquals("Invalid OTP", invalidOTPException.getMessage());
        assertNull(invalidOTPException.getCause());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        Throwable cause = new RuntimeException("Underlying OTP validation error");
        InvalidOTPException invalidOTPException = new InvalidOTPException(cause);

        assertEquals("Invalid OTP", invalidOTPException.getMessage());
        assertEquals(cause, invalidOTPException.getCause());
    }
}