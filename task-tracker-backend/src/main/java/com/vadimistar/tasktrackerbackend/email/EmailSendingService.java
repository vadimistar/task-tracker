package com.vadimistar.tasktrackerbackend.email;

public interface EmailSendingService {

    void sendEmail(EmailSendingTaskDto task);
}
