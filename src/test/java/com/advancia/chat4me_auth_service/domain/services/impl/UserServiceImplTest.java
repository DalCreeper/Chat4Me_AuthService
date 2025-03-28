package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.exceptions.JWTNotValidatedException;
import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.model.UserIdRequest;
import com.advancia.chat4me_auth_service.domain.repository.UsersRepoService;
import com.advancia.chat4me_auth_service.domain.services.AuthService;
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
    @Mock
    private AuthService authService; // Is necessary for be not null and mocked
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void shouldReturnAllUsers_whenIsAllOk() {
        UUID userId = UUID.fromString("7f113bb2-38eb-47e7-84a2-cf2703004b86");
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDE4NjUxMDksImV4cCI6MTc0MTk1MTUwOX0.das6YB90HEXhxzSOh8ukhHXmCjwPBmzHUx4yjIvaWJI";

        UserIdRequest userIDRequest = UserIdRequest.builder()
            .accessToken(accessToken)
            .build();
        List<User> users = List.of(
            User.builder()
                .id(userId)
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

        List<User> usersResult = userServiceImpl.getUsers(userIDRequest.getAccessToken());
        assertEquals(users, usersResult);

        verify(usersRepoService).getUsers();
    }

    @Test
    void shouldPropagateException_whenUsersRepoServiceFails() {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDE4NjUxMDksImV4cCI6MTc0MTk1MTUwOX0.das6YB90HEXhxzSOh8ukhHXmCjwPBmzHUx4yjIvaWJI";
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(usersRepoService).getUsers();

        Exception ex = assertThrows(RuntimeException.class, () -> userServiceImpl.getUsers(accessToken));
        assertSame(runtimeException, ex);

        verify(usersRepoService).getUsers();
    }

    @Test
    void shouldThrowJWTNotValidatedException_whenAccessTokenIsNull() {
        Exception ex = assertThrows(JWTNotValidatedException.class, () -> userServiceImpl.getUsers(null));
        assertNotNull(ex);
    }

    @Test
    void shouldThrowJWTNotValidatedException_whenAccessTokenIsEmpty() {
        Exception ex = assertThrows(JWTNotValidatedException.class, () -> userServiceImpl.getUsers(""));
        assertNotNull(ex);
    }
}