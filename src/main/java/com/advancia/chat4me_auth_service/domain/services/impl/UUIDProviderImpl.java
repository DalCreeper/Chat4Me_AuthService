package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.services.UUIDProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDProviderImpl implements UUIDProvider {
    @Override
    public UUID generateUUID() {
        return UUID.randomUUID();
    }
}