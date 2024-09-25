package com.vadimistar.tasktrackerbackend.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendEmailTask {

    private String destinationEmail;
    private String subject;
    private String text;
}
