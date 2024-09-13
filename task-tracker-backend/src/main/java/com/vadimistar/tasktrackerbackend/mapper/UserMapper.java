package com.vadimistar.tasktrackerbackend.mapper;

import com.vadimistar.tasktrackerbackend.dto.AuthorizeUserDto;
import com.vadimistar.tasktrackerbackend.dto.CurrentUserDto;
import com.vadimistar.tasktrackerbackend.dto.RegisterUserDto;
import com.vadimistar.tasktrackerbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User mapRegisterUserDtoToUser(RegisterUserDto registerUserDto);
    CurrentUserDto mapUserToCurrentUserDto(User user);
    AuthorizeUserDto mapRegisterUserDtoToAuthorizeUserDto(RegisterUserDto registerUserDto);
}
