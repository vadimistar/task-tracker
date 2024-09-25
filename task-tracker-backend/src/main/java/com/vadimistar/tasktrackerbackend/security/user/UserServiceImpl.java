package com.vadimistar.tasktrackerbackend.security.user;

import com.vadimistar.tasktrackerbackend.security.UserMapper;
import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public CurrentUserDto getCurrentUser(UserDetailsImpl userDetails) {
        return userMapper.mapUserDetailsImplToCurrentUserDto(userDetails);
    }
}
