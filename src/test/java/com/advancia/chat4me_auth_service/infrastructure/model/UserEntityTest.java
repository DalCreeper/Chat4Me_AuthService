package com.advancia.chat4me_auth_service.infrastructure.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserEntityTest {
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
            .id(UUID.randomUUID())
            .name("TestName")
            .surname("TestSurname")
            .username("TestUsername")
            .email("TestEmail")
            .password("TestPassword")
            .tokenId(UUID.randomUUID())
            .build();
    }

    @Test
    void shouldCreateUserEntityCorrectly_whenIsAllOk() {
        assertNotNull(userEntity.getId());
        assertEquals("TestName", userEntity.getName());
        assertEquals("TestSurname", userEntity.getSurname());
        assertEquals("TestUsername", userEntity.getUsername());
        assertEquals("TestEmail", userEntity.getEmail());
        assertNotNull(userEntity.getTokenId());
    }

    @Test
    void shouldEncryptPasswordCorrectly_whenIsAllOk() {
        String rawPassword = "TestPassword";
        userEntity.setPassword(rawPassword);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        assertNotEquals(rawPassword, userEntity.getPassword());
        assertTrue(encoder.matches(rawPassword, userEntity.getPassword()));
    }

    @Test
    void shouldValidatePasswordCorrectly_whenIsAllOk() {
        String rawPassword = "TestPassword";
        userEntity.setPassword(rawPassword);

        assertTrue(userEntity.checkPassword(rawPassword));
        assertFalse(userEntity.checkPassword("WrongPassword"));
    }
}