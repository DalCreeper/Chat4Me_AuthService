package com.advancia.chat4me_auth_service.domain.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class UUIDProviderImplTest {
    @InjectMocks
    private UUIDProviderImpl uuidProviderImpl;

    @Test
    void shouldReturnAValidUUID_whenIsAllOk() {
        UUID uuid = uuidProviderImpl.generateUUID();

        assertNotNull(uuid, "The generated UUID must not be null");
        assertDoesNotThrow(() -> UUID.fromString(uuid.toString()), "The UUID must have the correct format");
    }

    @Test
    void shouldReturnFixedUUID_whenIsAllOk() {
        UUID fixedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        try(MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(fixedUUID);

            UUID generatedUUID = uuidProviderImpl.generateUUID();

            assertEquals(fixedUUID, generatedUUID);
        }
    }
}