package com.vadimistar.tasktrackerbackend.service;

import com.vadimistar.tasktrackerbackend.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.dto.UpdateTaskDto;
import com.vadimistar.tasktrackerbackend.entity.Task;
import com.vadimistar.tasktrackerbackend.entity.User;
import com.vadimistar.tasktrackerbackend.exception.TaskNotFoundException;
import com.vadimistar.tasktrackerbackend.mapper.TaskMapper;
import com.vadimistar.tasktrackerbackend.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskDto> getTasks(User user) {
        return user.getTasks().stream().map(taskMapper::mapTaskToTaskDto).toList();
    }

    @Override
    @Transactional
    public TaskDto createTask(User user, CreateTaskDto taskDto) {
        Task task = taskMapper.mapCreateTaskDtoToTask(taskDto);
        task.setTitle(task.getTitle().trim());
        task.setOwner(user);
        if (task.getIsCompleted() == null) {
            task.setIsCompleted(false);
        }
        if (task.getIsCompleted()) {
            task.setCompletedAt(LocalDateTime.now());
        }
        Task savedTask = taskRepository.saveAndFlush(task);
        return taskMapper.mapTaskToTaskDto(savedTask);
    }

    @Override
    @Transactional
    public TaskDto updateTask(User user, UpdateTaskDto taskDto) {
        Task task = taskRepository.findByIdAndOwnerId(taskDto.getId(), user.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task with this id is not found"));
        boolean isJustCompleted = !task.getIsCompleted() && taskDto.getIsCompleted();
        taskMapper.mapUpdateTaskDtoToTask(taskDto, task);
        task.setTitle(task.getTitle().trim());
        if (isJustCompleted) {
            task.setCompletedAt(LocalDateTime.now());
        }
        if (!taskDto.getIsCompleted()) {
            task.setCompletedAt(null);
        }
        taskRepository.saveAndFlush(task);
        return taskMapper.mapTaskToTaskDto(task);
    }

    @Override
    @Transactional
    public void deleteTask(User user, DeleteTaskDto taskDto) {
        taskRepository.deleteByIdAndOwnerId(taskDto.getId(), user.getId());
    }
}
