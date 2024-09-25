package com.vadimistar.tasktrackerbackend.security;

import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
import com.vadimistar.tasktrackerbackend.security.user.LoginUserDto;
import com.vadimistar.tasktrackerbackend.security.user.RegisterUserDto;

public interface AuthService {

    JwtTokenDto loginUser(LoginUserDto loginUserDto);
    JwtTokenDto registerUser(RegisterUserDto registerUserDto);
}
