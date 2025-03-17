package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.services.SystemDateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class SystemDateTimeProviderImpl implements SystemDateTimeProvider {
    @Override
    public OffsetDateTime now() {
        return OffsetDateTime.now();
    }
}