package com.advancia.chat4me_auth_service.infrastructure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "otp_verifications")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerificationRequestEntity {
    @Id
    private UUID challengeId;
    private String otp;
    private long expiresAt;
    private UUID userId;
}
