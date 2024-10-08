package com.vadimistar.tasktrackeremailsender;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class SendEmailServiceImpl implements SendEmailService {

    private final JavaMailSender javaMailSender;
    private final EmailConfig emailConfig;

    @Override
    public void sendEmail(SendEmailTask task) {
        log.info("Received task with email to '{}' and subject '{}'", task.getDestinationEmail(), task.getSubject());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getFromAddress());
        message.setTo(task.getDestinationEmail());
        message.setSubject(task.getSubject());
        message.setText(task.getText());

        javaMailSender.send(message);

        log.info("Successfully send an email to '{}' with subject '{}'", task.getDestinationEmail(), task.getSubject());
    }
}
