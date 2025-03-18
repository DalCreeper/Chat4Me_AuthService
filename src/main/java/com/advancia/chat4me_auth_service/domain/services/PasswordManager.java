package com.advancia.chat4me_auth_service.domain.services;

public interface PasswordManager {
    String encodePassword(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}