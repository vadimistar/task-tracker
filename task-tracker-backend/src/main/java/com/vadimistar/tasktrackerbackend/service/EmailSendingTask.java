package com.vadimistar.tasktrackerbackend.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailSendingTask {

    private String destinationEmail;
    private String header;
    private String text;
}
