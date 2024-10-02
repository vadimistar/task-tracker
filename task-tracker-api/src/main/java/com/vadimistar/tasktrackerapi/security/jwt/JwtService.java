package com.vadimistar.tasktrackerapi.security.jwt;

import org.springframework.security.core.Authentication;

public interface JwtService {

    JwtTokenDto createToken(Authentication authentication);
    String getEmailFromToken(String token);
    boolean isTokenValid(String token);
}
