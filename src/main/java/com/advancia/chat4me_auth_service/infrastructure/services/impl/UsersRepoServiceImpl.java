package com.advancia.chat4me_auth_service.infrastructure.services.impl;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepoService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class UsersRepoServiceImpl implements UsersRepoService {
    @Override
    public List<User> getUsers() {
        return List.of();
    }
}
