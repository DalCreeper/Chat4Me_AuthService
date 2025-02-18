package com.advancia.chat4me_auth_service.domain.repository;

import com.advancia.chat4me_auth_service.domain.model.User;
import java.util.List;

public interface UsersRepoService {
    List<User> getUsers();
}
