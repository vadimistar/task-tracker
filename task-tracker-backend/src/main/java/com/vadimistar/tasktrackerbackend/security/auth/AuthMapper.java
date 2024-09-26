package com.vadimistar.tasktrackerbackend.security.auth;

import com.vadimistar.tasktrackerbackend.security.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {

    User mapRegisterUserDtoToUser(RegisterUserDto registerUserDto);
    LoginUserDto mapRegisterUserDtoToLoginUserDto(RegisterUserDto registerUserDto);
}
