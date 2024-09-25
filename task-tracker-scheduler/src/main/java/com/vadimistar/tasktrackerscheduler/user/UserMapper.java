package com.vadimistar.tasktrackerscheduler.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto mapUserToUserDto(User user);
}
