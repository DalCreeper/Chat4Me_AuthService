package com.advancia.chat4me_auth_service.infrastructure.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationRequestEntity {

    @Id
    private UUID tokenId;
    private String accessToken;
    private UUID userId;
}