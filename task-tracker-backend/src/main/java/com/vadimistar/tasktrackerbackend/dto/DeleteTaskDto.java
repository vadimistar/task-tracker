package com.vadimistar.tasktrackerbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteTaskDto {

    @NotNull(message = "Task id must be specified")
    private Long id;
}
