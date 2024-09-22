package com.vadimistar.tasktrackeremailsender;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class EmailConfig {

    @Value("${task-tracker.mail.from_address}")
    private String fromAddress;
}
