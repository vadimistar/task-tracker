package com.vadimistar.tasktrackerbackend.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTaskDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Builder.Default
    private String text = "";

    @JsonProperty("is_completed")
    @Builder.Default
    private Boolean isCompleted = false;
}
