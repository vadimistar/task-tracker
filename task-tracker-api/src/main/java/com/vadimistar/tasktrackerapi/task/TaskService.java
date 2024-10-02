package com.vadimistar.tasktrackerapi.task;

import com.vadimistar.tasktrackerapi.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerapi.task.dto.CreateTaskDto;
import com.vadimistar.tasktrackerapi.task.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerapi.task.dto.TaskDto;
import com.vadimistar.tasktrackerapi.task.dto.UpdateTaskDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(UserDetailsImpl userDetails);
    TaskDto createTask(UserDetailsImpl userDetails, CreateTaskDto taskDto);
    TaskDto updateTask(UserDetailsImpl userDetails, UpdateTaskDto taskDto);
    void deleteTask(UserDetailsImpl userDetails, DeleteTaskDto taskDto);
}
