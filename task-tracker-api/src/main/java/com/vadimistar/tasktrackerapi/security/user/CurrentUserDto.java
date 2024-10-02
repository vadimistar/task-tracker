package com.vadimistar.tasktrackerapi.security.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentUserDto {

    private long id;
    private String email;
}
