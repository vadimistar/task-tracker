package com.vadimistar.tasktrackerapi.security.user;

import com.vadimistar.tasktrackerapi.security.details.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    CurrentUserDto mapUserDetailsImplToCurrentUserDto(UserDetailsImpl userDetails);
}
