package com.advancia.chat4me_auth_service.application;

import com.advancia.Chat4Me_Auth_Service.generated.application.model.UserDto;
import com.advancia.chat4me_auth_service.application.mappers.UserMappers;
import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UsersApiDelegateImplTest {
    @Mock
    private UserService userService;
    @Mock
    private UserMappers userMappers;
    @InjectMocks
    private UsersApiDelegateImpl usersApiDelegateImpl;

    @Test
    void shouldReturnAvailableUserList_whenAllOk() {
        List<User> users = List.of(
            User.builder()
                .id(UUID.randomUUID())
                .name("testName")
                .surname("testSurname")
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .tokenId(UUID.randomUUID())
                .build(),
            User.builder()
                .id(UUID.randomUUID())
                .name("testName2")
                .surname("testSurname2")
                .username("testUsername2")
                .email("testEmail2")
                .password("testPassword2")
                .tokenId(UUID.randomUUID())
                .build()
        );
        List<UserDto> usersDto = new ArrayList<>();

        doReturn(users).when(userService).getUsers();
        doReturn(usersDto).when(userMappers).convertFromDomain(users);

        ResponseEntity<List<UserDto>> response = usersApiDelegateImpl.getUsers();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(usersDto, response.getBody());

        verify(userService).getUsers();
        verify(userMappers).convertFromDomain(users);
    }

    @Test
    void shouldPropagateException_whenUserServiceFails() {
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(userService).getUsers();

        Exception ex = assertThrows(RuntimeException.class, () -> usersApiDelegateImpl.getUsers());
        assertSame(runtimeException, ex);

        verify(userService).getUsers();
        verify(userMappers, never()).convertFromDomain(anyList());
    }

    @Test
    void shouldPropagateException_whenUserMappersFails() {
        List<User> users = List.of(
            User.builder()
                .id(UUID.randomUUID())
                .name("testName")
                .surname("testSurname")
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .tokenId(UUID.randomUUID())
                .build(),
            User.builder()
                .id(UUID.randomUUID())
                .name("testName2")
                .surname("testSurname2")
                .username("testUsername2")
                .email("testEmail2")
                .password("testPassword2")
                .tokenId(UUID.randomUUID())
                .build()
        );
        RuntimeException runtimeException = new RuntimeException("Service error");

        doReturn(users).when(userService).getUsers();
        doThrow(runtimeException).when(userMappers).convertFromDomain(users);

        Exception ex = assertThrows(RuntimeException.class, () -> usersApiDelegateImpl.getUsers());
        assertSame(runtimeException, ex);

        verify(userService).getUsers();
        verify(userMappers).convertFromDomain(users);
    }
}