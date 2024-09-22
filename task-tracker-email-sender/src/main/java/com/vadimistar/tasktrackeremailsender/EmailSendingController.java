package com.vadimistar.tasktrackeremailsender;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSendingController {

    private final EmailSendingService emailSendingService;

    @KafkaListener(topics = "EMAIL_SENDING_TASKS", groupId = "default")
    void consume(EmailSendingTask task) {
        emailSendingService.sendEmail(task);
    }
}
