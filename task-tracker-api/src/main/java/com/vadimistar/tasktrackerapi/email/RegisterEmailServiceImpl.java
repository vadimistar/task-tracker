package com.vadimistar.tasktrackerapi.email;

import com.vadimistar.tasktrackerapi.security.auth.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RegisterEmailServiceImpl implements RegisterEmailService {

    private final RegisterEmailConfig registerEmailConfig;
    private final KafkaTemplate<String, SendEmailTask> kafkaTemplate;

    @Override
    public void sendEmail(RegisterUserDto registerUserDto) {
        SendEmailTask task = SendEmailTask.builder()
                .destinationEmail(registerUserDto.getEmail())
                .subject(registerEmailConfig.getSubject())
                .text(registerEmailConfig.getText())
                .build();

        kafkaTemplate.send("EMAIL_SENDING_TASKS", task);
    }
}
