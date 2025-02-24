package com.advancia.chat4me_auth_service.infrastructure.services.impl;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepoService;
import com.advancia.chat4me_auth_service.domain.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@Primary
@RequiredArgsConstructor
public class AuthRepoServiceImpl implements AuthRepoService {
    private final AuthRepository authRepository;

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String encryptedPassword) {
        return authRepository.findByUsername(username)
            .filter(user -> user.checkPassword(encryptedPassword));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return authRepository.findById(id);
    }

    @Override
    public Optional<User> saveUser(User user) {
        User savedUser = authRepository.save(user);
        return Optional.of(savedUser);
    }
}