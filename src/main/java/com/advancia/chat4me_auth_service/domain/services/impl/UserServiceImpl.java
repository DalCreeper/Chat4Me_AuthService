package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepoService;
import com.advancia.chat4me_auth_service.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepoService usersRepoService;

    @Override
    public List<User> getUsers() {
        //List<User> users = usersRepoService.getUsers();   // TODO : ADATTARE POI CON LA PERSISTENZA
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            User user = User.builder()
                .name("Name " + i)
                .surname("Surname " + i)
                .email("Email " + i)
                .tokenID("Token " + i)
                .build();
            users.add(user);
        }
        return users;
    }
}