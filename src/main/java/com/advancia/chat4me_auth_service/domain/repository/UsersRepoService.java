package com.advancia.chat4me_auth_service.domain.repository;

import com.advancia.chat4me_auth_service.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersRepoService {
    List<User> getUsers();
}
