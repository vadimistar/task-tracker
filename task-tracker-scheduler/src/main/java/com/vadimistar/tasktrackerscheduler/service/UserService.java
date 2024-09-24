package com.vadimistar.tasktrackerscheduler.service;

import com.vadimistar.tasktrackerscheduler.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
}
