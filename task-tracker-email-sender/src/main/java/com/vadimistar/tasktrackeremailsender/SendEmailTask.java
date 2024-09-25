package com.vadimistar.tasktrackeremailsender;

import lombok.Data;

@Data
public class SendEmailTask {

    private String destinationEmail;
    private String header;
    private String text;
}
