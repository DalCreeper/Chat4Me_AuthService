package com.advancia.chat4me_auth_service.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeResponseEntity {
    private UUID challengeId;
    private String message;
    private UUID userId;
}
