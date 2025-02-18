package com.advancia.chat4me_auth_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerificationRequest {
    private String challengeId;

    private String otp;

    private UUID userId;
}
