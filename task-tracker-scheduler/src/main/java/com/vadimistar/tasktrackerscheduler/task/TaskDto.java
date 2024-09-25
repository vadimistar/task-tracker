package com.vadimistar.tasktrackerscheduler.task;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDto {

    private long id;
    private String title;
    private String text;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
}
