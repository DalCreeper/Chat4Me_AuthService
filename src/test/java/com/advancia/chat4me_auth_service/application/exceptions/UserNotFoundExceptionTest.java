package com.advancia.chat4me_auth_service.application.exceptions;

import com.advancia.chat4me_auth_service.application.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserNotFoundExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        UserNotFoundException userNotFoundException = new UserNotFoundException();

        assertEquals("User not found", userNotFoundException.getMessage());
        assertNull(userNotFoundException.getCause());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        Throwable cause = new RuntimeException("User ID not found in DB");
        UserNotFoundException userNotFoundException = new UserNotFoundException(cause);

        assertEquals("User not found", userNotFoundException.getMessage());
        assertEquals(cause, userNotFoundException.getCause());
    }
}