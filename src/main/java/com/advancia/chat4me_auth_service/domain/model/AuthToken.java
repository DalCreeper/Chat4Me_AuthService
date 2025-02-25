package com.advancia.chat4me_auth_service.domain.model;

import jakarta.persistence.*;
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
public class AuthToken {

    @Id
    private UUID tokenId;

    @Transient
    private String accessToken;

    private int expiresIn;

    private String message;

    private UUID userId;
}
