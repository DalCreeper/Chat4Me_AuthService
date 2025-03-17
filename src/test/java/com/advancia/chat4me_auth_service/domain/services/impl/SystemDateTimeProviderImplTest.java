package com.advancia.chat4me_auth_service.domain.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SystemDateTimeProviderImplTest {
    @InjectMocks
    private SystemDateTimeProviderImpl systemDateTimeProviderImpl;

    @Test
    void shouldReturnCurrentOffsetDateTime_whenAllOk() {
        OffsetDateTime beforeCall = OffsetDateTime.now();
        OffsetDateTime result = systemDateTimeProviderImpl.now();
        OffsetDateTime afterCall = OffsetDateTime.now();

        assertNotNull(result, "OffsetDateTime should not be null");
        assertTrue(
                !result.isBefore(beforeCall) && !result.isAfter(afterCall),
                "The returned OffsetDateTime must be between before and after the call"
        );
    }
}