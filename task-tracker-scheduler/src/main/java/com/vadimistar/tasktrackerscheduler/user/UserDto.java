package com.vadimistar.tasktrackerscheduler.user;

import com.vadimistar.tasktrackerscheduler.task.TaskDto;
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
