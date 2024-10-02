package com.vadimistar.tasktrackerapi.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "task-tracker.register-email")
@Getter
@Setter
public class RegisterEmailConfig {

    private String subject;
    private String text;
}
