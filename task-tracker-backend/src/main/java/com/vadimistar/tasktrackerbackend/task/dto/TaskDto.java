package com.vadimistar.tasktrackerbackend.task.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {

    private long id;
    private String title;
    private String text;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
}
