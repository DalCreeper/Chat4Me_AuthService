package com.advancia.chat4me_auth_service.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @Id
    private UUID refreshTokenId;

    private UUID userId;
}
