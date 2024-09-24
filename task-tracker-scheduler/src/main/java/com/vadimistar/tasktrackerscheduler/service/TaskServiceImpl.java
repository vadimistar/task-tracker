package com.vadimistar.tasktrackerscheduler.service;

import com.vadimistar.tasktrackerscheduler.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Override
    public List<TaskDto> getTasksCompletedLastDay(List<TaskDto> tasks) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayBefore = now.minusDays(1);

        return tasks.stream()
                .filter(task -> (task.getIsCompleted()) && task.getCompletedAt().isAfter(dayBefore))
                .toList();
    }
}
