package com.advancia.chat4me_auth_service.application.mappers;

import com.advancia.chat4me_auth_service.generated.application.model.UserDto;
import com.advancia.chat4me_auth_service.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserMappersImplTest {
    @InjectMocks
    private UserMappersImpl userMappersImpl;

    @Test
    void shouldConvertUserFromDomain_whenIsAllOk() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username("testUser")
            .email("testEmail")
            .password("testPassword")
            .build();

        UserDto userDto = userMappersImpl.convertFromDomain(user);
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getSurname(), userDto.getSurname());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getTokenId(), userDto.getTokenId());
    }

    @Test
    void shouldReturnNull_whenUserIsNull() {
        assertNull(userMappersImpl.convertFromDomain((User) null));
    }

    @Test
    void shouldConvertUserListFromDomain_whenIsAllOk() {
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

        List<UserDto> usersDto = userMappersImpl.convertFromDomain(users);
        assertNotNull(usersDto);
        assertEquals(2, usersDto.size());
    }

    @Test
    void shouldReturnNull_whenUserListIsNull() {
        assertNull(userMappersImpl.convertFromDomain((List<User>) null));
    }

    @Test
    void shouldReturnEmptyList_whenUserListIsEmpty() {
        List<UserDto> usersDto = userMappersImpl.convertFromDomain(new ArrayList<>());
        assertNotNull(usersDto);
        assertTrue(usersDto.isEmpty());
    }
}