package com.vadimistar.tasktrackerbackend.email;

import com.vadimistar.tasktrackerbackend.security.auth.RegisterUserDto;

public interface RegisterEmailService {

    void sendEmail(RegisterUserDto request);
}
