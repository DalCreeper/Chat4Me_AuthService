package com.advancia.chat4me_auth_service.domain.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PasswordManagerImplTest {
    @InjectMocks
    private PasswordManagerImpl passwordManagerImpl;

    @Test
    void shouldEncryptPasswordCorrectly_whenIsAllOk() {
        String rawPassword = "TestPassword";
        String encodedPassword = passwordManagerImpl.encodePassword(rawPassword);

        assertNotNull(encodedPassword, "The encrypted password must be null");
        assertNotEquals(rawPassword, encodedPassword, "The encrypted password must be different from the original password");
        assertTrue(passwordManagerImpl.matches(rawPassword, encodedPassword));
    }

    @Test
    void shouldValidatePasswordCorrectly_whenIsAllOk() {
        String rawPassword = "TestPassword";
        String encodedPassword = passwordManagerImpl.encodePassword(rawPassword);

        assertTrue(passwordManagerImpl.matches(rawPassword, encodedPassword), "Password should match");
        assertFalse(passwordManagerImpl.matches("WrongPassword", encodedPassword), "The incorrect password should not match");
    }
}