package com.vadimistar.tasktrackerscheduler.mapper;

import com.vadimistar.tasktrackerscheduler.dto.UserDto;
import com.vadimistar.tasktrackerscheduler.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto mapUserToUserDto(User user);
}
