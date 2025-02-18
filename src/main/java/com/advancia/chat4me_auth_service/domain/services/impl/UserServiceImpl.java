package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepoService;
import com.advancia.chat4me_auth_service.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepoService usersRepoService;

    @Override
    public List<User> getUsers(String email) {
        User user = User.builder()
            .name("Advancia")
            .surname("Prova")
            .build();

        return List.of(user);
    }
}
