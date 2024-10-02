package com.vadimistar.tasktrackerapi.email;

import com.vadimistar.tasktrackerapi.security.auth.RegisterUserDto;

public interface RegisterEmailService {

    void sendEmail(RegisterUserDto request);
}
