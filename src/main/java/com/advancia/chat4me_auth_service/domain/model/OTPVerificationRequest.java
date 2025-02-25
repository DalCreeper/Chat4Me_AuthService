package com.advancia.chat4me_auth_service.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "otp_verifications")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerificationRequest {

    @Id
    private UUID challengeId;

    private String otp;

    private int expiresAt;

    private UUID userId;
}
