package com.advancia.chat4me_auth_service.application.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ChallengeNotFoundExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        ChallengeNotFoundException challengeNotFoundException = new ChallengeNotFoundException();

        assertEquals("Challenge not found", challengeNotFoundException.getMessage());
        assertNull(challengeNotFoundException.getCause());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        Throwable cause = new RuntimeException("Underlying error");
        ChallengeNotFoundException challengeNotFoundException = new ChallengeNotFoundException(cause);

        assertEquals("Challenge not found", challengeNotFoundException.getMessage());
        assertEquals(cause, challengeNotFoundException.getCause());
    }
}