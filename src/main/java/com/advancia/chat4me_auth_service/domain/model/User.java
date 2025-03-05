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
public class User {
    private UUID id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String tokenId;
}