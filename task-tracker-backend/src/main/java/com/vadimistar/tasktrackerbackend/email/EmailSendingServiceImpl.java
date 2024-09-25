package com.vadimistar.tasktrackerbackend.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailSendingServiceImpl implements EmailSendingService {

    private final KafkaTemplate<String, EmailSendingTask> kafkaTemplate;

    @Override
    public void sendEmail(EmailSendingTask task) {
        log.info("Send task with email to '{}' and header '{}' to email sender", task.getDestinationEmail(), task.getHeader());

        kafkaTemplate.send("EMAIL_SENDING_TASKS", task);
    }
}
