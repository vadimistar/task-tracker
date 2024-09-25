package com.vadimistar.tasktrackeremailsender;

import lombok.Data;

@Data
public class SendEmailTask {

    private String destinationEmail;
    private String subject;
    private String text;
}
