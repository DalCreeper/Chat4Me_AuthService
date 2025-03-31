package com.advancia.chat4me_auth_service.application.exceptions;

import com.advancia.chat4me_auth_service.application.exceptions.JWTNotValidatedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JWTNotValidatedExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        JWTNotValidatedException jwtNotValidatedException = new JWTNotValidatedException();

        assertEquals("JWT not validated", jwtNotValidatedException.getMessage());
        assertNull(jwtNotValidatedException.getCause());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        Throwable cause = new RuntimeException("Underlying JWT validation error");
        JWTNotValidatedException jwtNotValidatedException = new JWTNotValidatedException(cause);

        assertEquals("JWT not validated", jwtNotValidatedException.getMessage());
        assertEquals(cause, jwtNotValidatedException.getCause());
    }

    @Test
    void shouldThrowAnExceptionWithPromptMessageAndCause_whenIsAllOk() {
        String customMessage = "Custom JWT validation error";
        Throwable cause = new RuntimeException("Underlying JWT issue");

        JWTNotValidatedException jwtNotValidatedException = new JWTNotValidatedException(customMessage, cause);

        assertEquals(customMessage, jwtNotValidatedException.getMessage());
        assertEquals(cause, jwtNotValidatedException.getCause());
    }
}