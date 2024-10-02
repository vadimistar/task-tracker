package com.vadimistar.tasktrackerapi.security.auth;

import com.vadimistar.tasktrackerapi.security.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {

    User mapRegisterUserDtoToUser(RegisterUserDto registerUserDto);
    LoginUserDto mapRegisterUserDtoToLoginUserDto(RegisterUserDto registerUserDto);
}
