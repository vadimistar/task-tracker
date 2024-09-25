package com.vadimistar.tasktrackerbackend.task;

import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerbackend.task.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.task.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerbackend.task.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.task.dto.UpdateTaskDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(UserDetailsImpl userDetails);
    TaskDto createTask(UserDetailsImpl userDetails, CreateTaskDto taskDto);
    TaskDto updateTask(UserDetailsImpl userDetails, UpdateTaskDto taskDto);
    void deleteTask(UserDetailsImpl userDetails, DeleteTaskDto id);
}
