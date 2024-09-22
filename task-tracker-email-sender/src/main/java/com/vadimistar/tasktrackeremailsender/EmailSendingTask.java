package com.vadimistar.tasktrackeremailsender;

import lombok.Data;

@Data
public class EmailSendingTask {

    private String destinationEmail;
    private String header;
    private String text;
}
