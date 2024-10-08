package com.vadimistar.tasktrackerapi.task;

import com.vadimistar.tasktrackerapi.security.details.UserDetailsMapper;
import com.vadimistar.tasktrackerapi.security.user.User;
import com.vadimistar.tasktrackerapi.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerapi.task.dto.CreateTaskDto;
import com.vadimistar.tasktrackerapi.task.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerapi.task.dto.TaskDto;
import com.vadimistar.tasktrackerapi.task.dto.UpdateTaskDto;
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
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public List<TaskDto> getTasks(UserDetailsImpl userDetails) {
        return taskRepository.findByOwnerId(userDetails.getId())
                .stream()
                .map(taskMapper::mapTaskToTaskDto)
                .toList();
    }

    @Override
    @Transactional
    public TaskDto createTask(UserDetailsImpl userDetails, CreateTaskDto taskDto) {
        Task task = taskMapper.mapCreateTaskDtoToTask(taskDto);
        task.setTitle(task.getTitle().trim());

        User owner = userDetailsMapper.mapUserDetailsImplToUser(userDetails);
        task.setOwner(owner);

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
    public TaskDto updateTask(UserDetailsImpl userDetails, UpdateTaskDto taskDto) {
        Task task = taskRepository.findByIdAndOwnerId(taskDto.getId(), userDetails.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task with this id is not found"));

        boolean isJustCompleted = !task.getIsCompleted() &&
                (taskDto.getIsCompleted() != null && taskDto.getIsCompleted());

        taskMapper.mapUpdateTaskDtoToTask(taskDto, task);
        task.setTitle(task.getTitle().trim());

        if (isJustCompleted) {
            task.setCompletedAt(LocalDateTime.now());
        }

        if (taskDto.getIsCompleted() != null && !taskDto.getIsCompleted()) {
            task.setCompletedAt(null);
        }

        taskRepository.saveAndFlush(task);
        return taskMapper.mapTaskToTaskDto(task);
    }

    @Override
    @Transactional
    public void deleteTask(UserDetailsImpl userDetails, DeleteTaskDto taskDto) {
        if (!taskRepository.existsByIdAndOwnerId(taskDto.getId(), userDetails.getId())) {
            throw new TaskNotFoundException("Task with this id is not found");
        }

        taskRepository.deleteById(taskDto.getId());
    }
}
