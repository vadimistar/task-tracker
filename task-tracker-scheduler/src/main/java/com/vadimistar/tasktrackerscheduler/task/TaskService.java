package com.vadimistar.tasktrackerscheduler.task;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasksCompletedLastDay(List<TaskDto> tasks);
}
