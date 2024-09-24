package com.vadimistar.tasktrackerscheduler.service;

import com.vadimistar.tasktrackerscheduler.dto.EmailSendingTaskDto;

public interface EmailSendingService {

    void sendEmail(EmailSendingTaskDto task);
}
