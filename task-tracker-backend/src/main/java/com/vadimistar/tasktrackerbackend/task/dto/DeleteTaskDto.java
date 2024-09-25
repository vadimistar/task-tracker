package com.vadimistar.tasktrackerbackend.task.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteTaskDto {

    @NotNull(message = "Task id must be specified")
    private Long id;
}
