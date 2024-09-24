package com.vadimistar.tasktrackerscheduler.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {

    private long id;
    private String email;
    private List<TaskDto> tasks;
}
