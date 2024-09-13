package com.vadimistar.tasktrackerbackend.service;

import com.vadimistar.tasktrackerbackend.dto.JwtTokenDto;
import org.springframework.security.core.Authentication;

public interface JwtService {

    JwtTokenDto createToken(Authentication authentication);
    String getEmailFromToken(String token);
    boolean isTokenValid(String token);
}
