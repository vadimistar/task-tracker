package com.vadimistar.tasktrackerbackend.security.user;

import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    CurrentUserDto mapUserDetailsImplToCurrentUserDto(UserDetailsImpl userDetails);
}
