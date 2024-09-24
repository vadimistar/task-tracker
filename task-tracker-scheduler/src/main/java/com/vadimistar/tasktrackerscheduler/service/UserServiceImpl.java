package com.vadimistar.tasktrackerscheduler.service;

import com.vadimistar.tasktrackerscheduler.dto.UserDto;
import com.vadimistar.tasktrackerscheduler.mapper.UserMapper;
import com.vadimistar.tasktrackerscheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapUserToUserDto)
                .toList();
    }
}
