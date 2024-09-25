package com.vadimistar.tasktrackerbackend.service;

import com.vadimistar.tasktrackerbackend.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerbackend.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.dto.UpdateTaskDto;
import com.vadimistar.tasktrackerbackend.entity.UserDetailsImpl;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(UserDetailsImpl userDetails);
    TaskDto createTask(UserDetailsImpl userDetails, CreateTaskDto taskDto);
    TaskDto updateTask(UserDetailsImpl userDetails, UpdateTaskDto taskDto);
    void deleteTask(UserDetailsImpl userDetails, DeleteTaskDto id);
}
