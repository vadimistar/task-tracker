package com.vadimistar.tasktrackerbackend.mapper;

import com.vadimistar.tasktrackerbackend.dto.AuthorizeUserDto;
import com.vadimistar.tasktrackerbackend.dto.CurrentUserDto;
import com.vadimistar.tasktrackerbackend.dto.RegisterUserDto;
import com.vadimistar.tasktrackerbackend.entity.User;
import com.vadimistar.tasktrackerbackend.entity.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User mapRegisterUserDtoToUser(RegisterUserDto registerUserDto);
    CurrentUserDto mapUserToCurrentUserDto(User user);
    AuthorizeUserDto mapRegisterUserDtoToAuthorizeUserDto(RegisterUserDto registerUserDto);
    UserDetailsImpl mapUserToUserDetailsImpl(User user);
    User mapUserDetailsImplToUser(UserDetailsImpl userDetailsImpl);
    CurrentUserDto mapUserDetailsImplToCurrentUserDto(UserDetailsImpl userDetails);
}
