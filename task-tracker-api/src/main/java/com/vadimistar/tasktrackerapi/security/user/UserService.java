package com.vadimistar.tasktrackerapi.security.user;

import com.vadimistar.tasktrackerapi.security.details.UserDetailsImpl;

public interface UserService {

    CurrentUserDto getCurrentUser(UserDetailsImpl user);
}
