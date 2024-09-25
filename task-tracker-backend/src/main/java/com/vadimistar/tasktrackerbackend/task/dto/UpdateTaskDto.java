package com.vadimistar.tasktrackerbackend.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vadimistar.tasktrackerbackend.task.validation.NullOrNotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskDto {

    @NotNull(message = "Task id must be specified")
    private Long id;

    @NullOrNotBlank(message = "Title cannot be empty")
    private String title;

    private String text;

    @JsonProperty("is_completed")
    private Boolean isCompleted;
}
