package com.vadimistar.tasktrackerbackend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class JwtTokenDto {

    private String token;
    private Duration expiresIn;
}
