package com.advancia.chat4me_auth_service.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InvalidCredentialExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        InvalidCredentialException invalidCredentialException = new InvalidCredentialException();

        assertEquals("Invalid credentials", invalidCredentialException.getMessage());
        assertNull(invalidCredentialException.getCause());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        Throwable cause = new RuntimeException("Underlying authentication issue");
        InvalidCredentialException invalidCredentialException = new InvalidCredentialException(cause);

        assertEquals("Invalid credentials", invalidCredentialException.getMessage());
        assertEquals(cause, invalidCredentialException.getCause());
    }
}