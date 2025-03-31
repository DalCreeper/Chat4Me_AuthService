package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.application.exceptions.JWTNotValidatedException;
import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.model.UserIdRequest;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepoService;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
import com.advancia.chat4me_auth_service.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepoService usersRepoService;
    private final AuthService authService;

    @Override
    public List<User> getUsers(String accessToken) {
        if(accessToken == null || accessToken.isEmpty()) {
            throw buildJWTNotValidatedException();
        } else {
            UserIdRequest userIDRequest = new UserIdRequest(accessToken);
            authService.extractUUID(userIDRequest);
            return usersRepoService.getUsers();
        }
    }

    public JWTNotValidatedException buildJWTNotValidatedException() {
        return new JWTNotValidatedException();
    }
}