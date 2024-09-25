package com.vadimistar.tasktrackerbackend.security.auth;

import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;

public interface AuthService {

    JwtTokenDto loginUser(LoginUserDto loginUserDto);
    JwtTokenDto registerUser(RegisterUserDto registerUserDto);
}
