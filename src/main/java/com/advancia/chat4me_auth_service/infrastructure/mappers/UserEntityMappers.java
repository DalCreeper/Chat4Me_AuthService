package com.advancia.chat4me_auth_service.infrastructure.mappers;

import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.infrastructure.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserEntityMappers {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "tokenId", target = "tokenId")
    User convertFromInfrastructure(UserEntity userEntity);
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "tokenId", target = "tokenId")
    UserEntity convertToInfrastructure(User user);
    List<User> convertFromInfrastructure(List<UserEntity> usersEntity);
}