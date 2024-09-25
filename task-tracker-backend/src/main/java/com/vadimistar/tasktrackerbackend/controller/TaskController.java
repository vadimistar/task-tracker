package com.vadimistar.tasktrackerbackend.controller;

import com.vadimistar.tasktrackerbackend.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.dto.UpdateTaskDto;
import com.vadimistar.tasktrackerbackend.entity.User;
import com.vadimistar.tasktrackerbackend.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerbackend.entity.UserDetailsImpl;
import com.vadimistar.tasktrackerbackend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    public List<TaskDto> getTasks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return taskService.getTasks(userDetails);
    }

    @PostMapping("/task")
    public TaskDto createTask(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid CreateTaskDto taskDto) {
        return taskService.createTask(userDetails, taskDto);
    }

    @PatchMapping("/task")
    public TaskDto updateTask(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid UpdateTaskDto taskDto) {
        return taskService.updateTask(userDetails, taskDto);
    }

    @DeleteMapping("/task")
    public void deleteTask(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid DeleteTaskDto taskDto) {
        taskService.deleteTask(userDetails, taskDto);
    }
}
