package com.vadimistar.tasktrackerapi.security.auth;

import com.vadimistar.tasktrackerapi.security.jwt.JwtTokenDto;

public interface AuthService {

    JwtTokenDto loginUser(LoginUserDto loginUserDto);
    JwtTokenDto registerUser(RegisterUserDto registerUserDto);
}
