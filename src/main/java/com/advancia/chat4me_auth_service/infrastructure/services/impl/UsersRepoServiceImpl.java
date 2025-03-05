package com.advancia.chat4me_auth_service.infrastructure.services.impl;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepoService;
import com.advancia.chat4me_auth_service.infrastructure.mappers.UserEntityMappers;
import com.advancia.chat4me_auth_service.infrastructure.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class UsersRepoServiceImpl implements UsersRepoService {
    private final UsersRepository usersRepository;
    private final UserEntityMappers userEntityMappers;

    @Override
    public List<User> getUsers() {
        return userEntityMappers.convertFromInfrastructure(usersRepository.getUsers());
    }
}