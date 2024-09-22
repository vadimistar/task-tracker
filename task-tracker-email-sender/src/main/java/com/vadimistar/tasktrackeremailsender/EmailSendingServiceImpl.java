package com.vadimistar.tasktrackeremailsender;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailSendingServiceImpl implements EmailSendingService {

    private final JavaMailSender javaMailSender;
    private final EmailConfig emailConfig;

    @Override
    public void sendEmail(EmailSendingTask task) {
        log.info("Received task with email to '{}' and header '{}'", task.getDestinationEmail(), task.getHeader());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getFromAddress());
        message.setTo(task.getDestinationEmail());
        message.setSubject(task.getHeader());
        message.setText(task.getText());

        javaMailSender.send(message);

        log.info("Successfully send an email to '{}' with header '{}'", task.getDestinationEmail(), task.getHeader());
    }
}
