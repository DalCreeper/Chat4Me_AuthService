package com.advancia.chat4me_auth_service.infrastructure.services.impl;

import com.advancia.chat4me_auth_service.application.exceptions.UserNotFoundException;
import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.infrastructure.mappers.UserEntityMappers;
import com.advancia.chat4me_auth_service.infrastructure.model.UserEntity;
import com.advancia.chat4me_auth_service.infrastructure.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersRepoServiceImplTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private UserEntityMappers userEntityMappers;
    @InjectMocks
    private UsersRepoServiceImpl usersRepoServiceImpl;

    @Test
    void shouldReturnAllUsers_whenIsAllOk() {
        List<UserEntity> usersEntity = List.of(
            UserEntity.builder()
                .id(UUID.randomUUID())
                .name("testName")
                .surname("testSurname")
                .username("testUser")
                .email("testEmail")
                .password("testPassword")
                .build(),
            UserEntity.builder()
                .id(UUID.randomUUID())
                .name("testName2")
                .surname("testSurname2")
                .username("testUser2")
                .email("testEmail2")
                .password("testPassword2")
                .build()
        );
        List<User> users = List.of(
            User.builder()
                .id(usersEntity.get(0).getId())
                .name(usersEntity.get(0).getName())
                .surname(usersEntity.get(0).getSurname())
                .username(usersEntity.get(0).getUsername())
                .email(usersEntity.get(0).getEmail())
                .password(usersEntity.get(0).getPassword())
                .build(),
            User.builder()
                .id(usersEntity.get(1).getId())
                .name(usersEntity.get(1).getName())
                .surname(usersEntity.get(1).getSurname())
                .username(usersEntity.get(1).getUsername())
                .email(usersEntity.get(1).getEmail())
                .password(usersEntity.get(1).getPassword())
                .build()
        );

        doReturn(usersEntity).when(usersRepository).getUsers();
        doReturn(users).when(userEntityMappers).convertFromInfrastructure(usersEntity);

        List<User> usersResult = usersRepoServiceImpl.getUsers();
        assertEquals(users, usersResult);

        verify(usersRepository).getUsers();
        verify(userEntityMappers).convertFromInfrastructure(usersEntity);
    }

    @Test
    void shouldPropagateException_whenUserRepositoryFails() {
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(usersRepository).getUsers();

        Exception ex = assertThrows(RuntimeException.class, () -> usersRepoServiceImpl.getUsers());
        assertSame(runtimeException, ex);

        verify(usersRepository).getUsers();
        verify(userEntityMappers, never()).convertFromInfrastructure(any(UserEntity.class));
    }

    @Test
    void shouldReturnUser_whenUserExists() {
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = UserEntity.builder()
            .id(userId)
            .name("testName")
            .surname("testSurname")
            .username("testUser")
            .email("testEmail")
            .password("testPassword")
            .build();
        User expectedUser = User.builder()
            .id(userId)
            .name("testName")
            .surname("testSurname")
            .username("testUser")
            .email("testEmail")
            .password("testPassword")
            .build();

        doReturn(Optional.of(userEntity)).when(usersRepository).findById(userId);
        doReturn(expectedUser).when(userEntityMappers).convertFromInfrastructure(userEntity);

        User userResult = usersRepoServiceImpl.getUser(userId);
        assertEquals(expectedUser, userResult);

        verify(usersRepository).findById(userId);
        verify(userEntityMappers).convertFromInfrastructure(userEntity);
    }

    @Test
    void shouldThrowException_whenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        doReturn(Optional.empty()).when(usersRepository).findById(userId);

        assertThrows(UserNotFoundException.class, () -> usersRepoServiceImpl.getUser(userId));

        verify(usersRepository).findById(userId);
        verify(userEntityMappers, never()).convertFromInfrastructure(any(UserEntity.class));
    }
}