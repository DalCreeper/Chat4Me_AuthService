package com.advancia.chat4me_auth_service.domain.services;

import java.time.OffsetDateTime;

public interface SystemDateTimeProvider {
    OffsetDateTime now();
}
