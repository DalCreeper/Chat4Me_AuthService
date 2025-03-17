package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UsersRepoService usersRepoService;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void shouldReturnAllUsers_whenIsAllOk() {
        List<User> users = List.of(
            User.builder()
                .id(UUID.randomUUID())
                .name("testName")
                .surname("testSurname")
                .username("testUser")
                .email("testEmail")
                .password("testPassword")
                .build(),
            User.builder()
                .id(UUID.randomUUID())
                .name("testName2")
                .surname("testSurname2")
                .username("testUser2")
                .email("testEmail2")
                .password("testPassword2")
                .build()
        );

        doReturn(users).when(usersRepoService).getUsers();

        List<User> usersResult = userServiceImpl.getUsers();
        assertEquals(users, usersResult);

        verify(usersRepoService).getUsers();
    }

    @Test
    void shouldPropagateException_whenUsersRepoServiceFails() {
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(usersRepoService).getUsers();

        Exception ex = assertThrows(RuntimeException.class, () -> userServiceImpl.getUsers());
        assertSame(runtimeException, ex);

        verify(usersRepoService).getUsers();
    }
}