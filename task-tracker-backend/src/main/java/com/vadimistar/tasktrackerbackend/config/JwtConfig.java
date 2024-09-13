package com.vadimistar.tasktrackerbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "task-tracker.jwt")
@Getter
@Setter
public class JwtConfig {

    private String secret;
    private Duration expiresIn;
}
