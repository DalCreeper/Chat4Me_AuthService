package com.advancia.chat4me_auth_service.domain.services;

import com.advancia.chat4me_auth_service.domain.model.User;

import java.util.List;

public interface UserService {
    public List<User> getUsers ();
}
