package com.vadimistar.tasktrackerapi.security.details;

import com.vadimistar.tasktrackerapi.security.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDetailsMapper {

    UserDetailsImpl mapUserToUserDetailsImpl(User user);
    User mapUserDetailsImplToUser(UserDetailsImpl userDetailsImpl);
}
