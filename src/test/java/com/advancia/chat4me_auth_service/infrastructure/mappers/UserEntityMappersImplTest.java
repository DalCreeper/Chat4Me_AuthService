package com.advancia.chat4me_auth_service.infrastructure.mappers;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.infrastructure.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserEntityMappersImplTest {
    @InjectMocks
    private UserEntityMappersImpl userEntityMappersImpl;

    @Test
    void shouldConvertUserFromInfrastructure_whenIsAllOk() {
        UserEntity userEntity = UserEntity.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username("testUsername")
            .email("testEmail")
            .password("testPassword")
            .tokenId(UUID.randomUUID())
            .build();

        User user = userEntityMappersImpl.convertFromInfrastructure(userEntity);
        assertNotNull(user);
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getName(), user.getName());
        assertEquals(userEntity.getSurname(), user.getSurname());
        assertEquals(userEntity.getUsername(), user.getUsername());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getPassword(), user.getPassword());
        assertEquals(userEntity.getTokenId(), user.getTokenId());
    }

    @Test
    void shouldReturnNull_whenUserEntityIsNull() {
        assertNull(userEntityMappersImpl.convertFromInfrastructure((UserEntity) null));
    }

    @Test
    void shouldConvertUserToInfrastructure_whenIsAllOk() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username("testUsername")
            .email("testEmail")
            .password("testPassword")
            .tokenId(UUID.randomUUID())
            .build();

        UserEntity userEntity = userEntityMappersImpl.convertToInfrastructure(user);
        assertNotNull(userEntity);
        assertEquals(user.getId(), userEntity.getId());
        assertEquals(user.getName(), userEntity.getName());
        assertEquals(user.getSurname(), userEntity.getSurname());
        assertEquals(user.getUsername(), userEntity.getUsername());
        assertEquals(user.getEmail(), userEntity.getEmail());
        assertEquals(user.getPassword(), userEntity.getPassword());
        assertEquals(user.getTokenId(), userEntity.getTokenId());
    }

    @Test
    void shouldReturnNull_whenUserIsNull() {
        assertNull(userEntityMappersImpl.convertToInfrastructure((User) null));
    }

    @Test
    void shouldConvertListOfUsersFromInfrastructure_whenIsAllOk() {
        List<UserEntity> usersEntity = List.of(
            UserEntity.builder()
                .id(UUID.randomUUID())
                .name("testName")
                .surname("testSurname")
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .tokenId(UUID.randomUUID())
                .build(),
            UserEntity.builder()
                .id(UUID.randomUUID())
                .name("testName2")
                .surname("testSurname2")
                .username("testUsername2")
                .email("testEmail2")
                .password("testPassword2")
                .tokenId(UUID.randomUUID())
                .build()
        );

        List<User> users = userEntityMappersImpl.convertFromInfrastructure(usersEntity);
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(usersEntity.get(0).getId(), users.get(0).getId());
        assertEquals(usersEntity.get(1).getId(), users.get(1).getId());
    }

    @Test
    void shouldReturnEmptyList_whenUserEntityListIsEmpty() {
        List<UserEntity> usersEntity = List.of();

        List<User> users = userEntityMappersImpl.convertFromInfrastructure(usersEntity);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void shouldReturnNull_whenUserEntityListIsNull() {
        assertNull(userEntityMappersImpl.convertFromInfrastructure((List<UserEntity>) null));
    }
}