package com.vadimistar.tasktrackerbackend.security.user;

import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;

public interface UserService {

    CurrentUserDto getCurrentUser(UserDetailsImpl user);
}
