package com.vadimistar.tasktrackerapi.security.user;

import com.vadimistar.tasktrackerapi.security.details.UserDetailsImpl;
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
