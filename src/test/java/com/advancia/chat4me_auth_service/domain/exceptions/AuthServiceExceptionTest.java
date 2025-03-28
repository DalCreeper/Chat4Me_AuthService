package com.advancia.chat4me_auth_service.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthServiceExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        String errorMessage = "Authentication failed";
        AuthServiceException authServiceException = new AuthServiceException(errorMessage);

        assertEquals(errorMessage, authServiceException.getMessage());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        String errorMessage = "Authentication error";
        Throwable cause = new RuntimeException("Root cause");
        AuthServiceException authServiceException = new AuthServiceException(errorMessage, cause);

        assertEquals(errorMessage, authServiceException.getMessage());
        assertEquals(cause, authServiceException.getCause());
    }
}