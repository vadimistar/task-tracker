package com.vadimistar.tasktrackerbackend.controller;

import com.vadimistar.tasktrackerbackend.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.dto.UpdateTaskDto;
import com.vadimistar.tasktrackerbackend.entity.User;
import com.vadimistar.tasktrackerbackend.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerbackend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    public List<TaskDto> getTasks(@AuthenticationPrincipal User user) {
        return taskService.getTasks(user);
    }

    @PostMapping("/task")
    public TaskDto createTask(@AuthenticationPrincipal User user, @Valid @RequestBody CreateTaskDto taskDto) {
        return taskService.createTask(user, taskDto);
    }

    @PatchMapping("/task")
    public TaskDto updateTask(@AuthenticationPrincipal User user, @Valid @RequestBody UpdateTaskDto taskDto) {
        return taskService.updateTask(user, taskDto);
    }

    @DeleteMapping("/task")
    public void deleteTask(@AuthenticationPrincipal User user, @Valid DeleteTaskDto taskDto) {
        taskService.deleteTask(user, taskDto);
    }
}
