package com.vadimistar.tasktrackerscheduler.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "task-tracker.report-email")
@Getter
@Setter
public class ReportEmailConfig {

    private String subject;
    private int activeTasksLimit;
    private int todayCompletedTasksLimit;
}
