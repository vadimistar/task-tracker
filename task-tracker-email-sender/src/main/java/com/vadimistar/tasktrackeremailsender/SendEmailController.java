package com.vadimistar.tasktrackeremailsender;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEmailController {

    private final SendEmailService sendEmailService;

    @KafkaListener(topics = "EMAIL_SENDING_TASKS", groupId = "default")
    void consume(SendEmailTask task) {
        sendEmailService.sendEmail(task);
    }
}
