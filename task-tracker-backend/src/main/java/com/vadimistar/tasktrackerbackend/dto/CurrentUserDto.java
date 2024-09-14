package com.vadimistar.tasktrackerbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentUserDto {

    private long id;
    private String email;
}
