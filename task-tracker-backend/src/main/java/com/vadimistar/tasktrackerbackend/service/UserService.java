package com.vadimistar.tasktrackerbackend.service;

import com.vadimistar.tasktrackerbackend.dto.AuthorizeUserDto;
import com.vadimistar.tasktrackerbackend.dto.CurrentUserDto;
import com.vadimistar.tasktrackerbackend.dto.RegisterUserDto;
import com.vadimistar.tasktrackerbackend.dto.JwtTokenDto;
import com.vadimistar.tasktrackerbackend.entity.User;

public interface UserService {

    JwtTokenDto registerUser(RegisterUserDto registerUserDto);
    JwtTokenDto authorizeUser(AuthorizeUserDto authorizeUserDto);
    CurrentUserDto getCurrentUser(User user);
}
