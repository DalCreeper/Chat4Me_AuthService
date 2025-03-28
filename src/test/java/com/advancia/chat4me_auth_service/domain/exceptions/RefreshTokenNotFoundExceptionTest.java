package com.advancia.chat4me_auth_service.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RefreshTokenNotFoundExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        RefreshTokenNotFoundException refreshTokenNotFoundException = new RefreshTokenNotFoundException();

        assertEquals("Refresh token not found", refreshTokenNotFoundException.getMessage());
        assertNull(refreshTokenNotFoundException.getCause());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        Throwable cause = new RuntimeException("Underlying refresh token error");
        RefreshTokenNotFoundException refreshTokenNotFoundException = new RefreshTokenNotFoundException(cause);

        assertEquals("Refresh token not found", refreshTokenNotFoundException.getMessage());
        assertEquals(cause, refreshTokenNotFoundException.getCause());
    }
}