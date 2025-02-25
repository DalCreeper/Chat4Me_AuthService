package com.advancia.chat4me_auth_service.infrastructure.services.impl;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class AuthRepoServiceImpl implements AuthRepoService {
    private final UsersRepository usersRepository;

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String encryptedPassword) {
        return usersRepository.findByUsername(username)
            .filter(user -> user.checkPassword(encryptedPassword));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return usersRepository.findById(id);
    }

    @Override
    public Optional<User> saveUser(User user) {
        User savedUser = usersRepository.save(user);
        return Optional.of(savedUser);
    }
}