package com.advancia.chat4me_auth_service.application.mappers;

import com.advancia.chat4me_auth_service.generated.application.model.UserDto;
import com.advancia.chat4me_auth_service.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserMappers {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "tokenId", target = "tokenId")
    UserDto convertFromDomain(User user);
    List<UserDto> convertFromDomain(List<User> users);
}