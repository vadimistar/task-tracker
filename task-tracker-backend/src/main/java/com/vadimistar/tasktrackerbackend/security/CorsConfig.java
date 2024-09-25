package com.vadimistar.tasktrackerbackend.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Component
public class CorsConfig implements CorsConfigurationSource {

    @Value("#{'${task-tracker.cors.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
        config.setAllowedHeaders(List.of("Cookie", "Content-Type"));
        config.setAllowCredentials(true);

        return config;
    }
}
