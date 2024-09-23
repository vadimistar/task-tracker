package com.vadimistar.tasktrackerbackend.service;

import com.vadimistar.tasktrackerbackend.dto.EmailSendingTaskDto;

public interface EmailSendingService {

    void sendEmail(EmailSendingTaskDto task);
}
