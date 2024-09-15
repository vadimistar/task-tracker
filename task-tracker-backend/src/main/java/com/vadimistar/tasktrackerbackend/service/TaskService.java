package com.vadimistar.tasktrackerbackend.service;

import com.vadimistar.tasktrackerbackend.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerbackend.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.dto.UpdateTaskDto;
import com.vadimistar.tasktrackerbackend.entity.User;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(User user);
    TaskDto createTask(User user, CreateTaskDto taskDto);
    TaskDto updateTask(User user, UpdateTaskDto taskDto);
    void deleteTask(User user, DeleteTaskDto id);
}
