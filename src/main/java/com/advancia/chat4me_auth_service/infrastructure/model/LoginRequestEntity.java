package com.advancia.chat4me_auth_service.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestEntity {
    private String username;
    private String password;
}
