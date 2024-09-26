package com.vadimistar.tasktrackerscheduler.email;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ReportEmailConfig {

    @Value("${task-tracker.report-email.subject}")
    private String subject;
}
