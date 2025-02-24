package com.advancia.chat4me_auth_service.domain.repository;

import com.advancia.chat4me_auth_service.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface AuthRepoService {
    Optional<User> findByUsernameAndPassword(String username, String encryptedPassword);
    Optional<User> findById(UUID id);
    Optional<User> saveUser(User user);
}
