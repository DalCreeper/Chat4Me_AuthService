package com.advancia.chat4me_auth_service.application;

import com.advancia.chat4me_auth_service.generated.application.api.UsersApiDelegate;
import com.advancia.chat4me_auth_service.generated.application.model.UserDto;
import com.advancia.chat4me_auth_service.application.mappers.UserMappers;
import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersApiDelegateImpl implements UsersApiDelegate {
    private final UserService userService;
    private final UserMappers userMappers;

    @Override
    public ResponseEntity<List<UserDto>> getUsers(String accessToken) {
        List<User> users = userService.getUsers(accessToken);
        List<UserDto> usersDto = userMappers.convertFromDomain(users);
        return ResponseEntity.ok(usersDto);
    }
}