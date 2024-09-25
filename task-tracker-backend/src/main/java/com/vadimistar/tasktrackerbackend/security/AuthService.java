package com.vadimistar.tasktrackerbackend.security;

import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
import com.vadimistar.tasktrackerbackend.security.user.AuthorizeUserDto;
import com.vadimistar.tasktrackerbackend.security.user.RegisterUserDto;

public interface AuthService {

    JwtTokenDto authorizeUser(AuthorizeUserDto authorizeUserDto);
    JwtTokenDto registerUser(RegisterUserDto registerUserDto);
}
