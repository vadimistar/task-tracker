package com.vadimistar.tasktrackerbackend.security;

import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerbackend.security.auth.LoginUserDto;
import com.vadimistar.tasktrackerbackend.security.auth.RegisterUserDto;
import com.vadimistar.tasktrackerbackend.security.user.CurrentUserDto;
import com.vadimistar.tasktrackerbackend.security.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User mapRegisterUserDtoToUser(RegisterUserDto registerUserDto);
    LoginUserDto mapRegisterUserDtoToLoginUserDto(RegisterUserDto registerUserDto);
    UserDetailsImpl mapUserToUserDetailsImpl(User user);
    User mapUserDetailsImplToUser(UserDetailsImpl userDetailsImpl);
    CurrentUserDto mapUserDetailsImplToCurrentUserDto(UserDetailsImpl userDetails);
}
