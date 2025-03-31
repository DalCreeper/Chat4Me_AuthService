package com.advancia.chat4me_auth_service.application.exceptions;

public class ChallengeNotFoundException extends AuthServiceException {
    public ChallengeNotFoundException() {
        super("Challenge not found");
    }

    public ChallengeNotFoundException(Throwable throwable) {
        super("Challenge not found", throwable);
    }
}