package com.vadimistar.tasktrackerbackend.security.user;

import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerbackend.security.auth.LoginUserDto;
import com.vadimistar.tasktrackerbackend.security.auth.RegisterUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User mapRegisterUserDtoToUser(RegisterUserDto registerUserDto);
    CurrentUserDto mapUserToCurrentUserDto(User user);
    LoginUserDto mapRegisterUserDtoToAuthorizeUserDto(RegisterUserDto registerUserDto);
    UserDetailsImpl mapUserToUserDetailsImpl(User user);
    User mapUserDetailsImplToUser(UserDetailsImpl userDetailsImpl);
    CurrentUserDto mapUserDetailsImplToCurrentUserDto(UserDetailsImpl userDetails);
}
