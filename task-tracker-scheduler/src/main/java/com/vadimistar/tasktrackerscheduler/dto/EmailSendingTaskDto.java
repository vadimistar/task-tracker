package com.vadimistar.tasktrackerscheduler.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailSendingTaskDto {

    private String destinationEmail;
    private String header;
    private String text;
}
