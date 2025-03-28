package com.advancia.chat4me_auth_service.infrastructure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "auth_tokens")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenEntity {
    @Id
    private UUID tokenId;

    @Transient
    private String accessToken;
    private long expiresIn;
    private String message;
    private UUID userId;
}
