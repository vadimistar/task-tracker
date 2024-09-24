package com.vadimistar.tasktrackerscheduler.service;

import com.vadimistar.tasktrackerscheduler.dto.TaskDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasksCompletedLastDay(List<TaskDto> tasks);
}
