package com.vadimistar.tasktrackerbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTaskDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    private String text = "";

    @JsonProperty("is_completed")
    private boolean isCompleted = false;
}
