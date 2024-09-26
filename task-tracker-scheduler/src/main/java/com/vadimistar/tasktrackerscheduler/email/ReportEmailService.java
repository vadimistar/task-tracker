package com.vadimistar.tasktrackerscheduler.email;

import com.vadimistar.tasktrackerscheduler.user.UserDto;

public interface ReportEmailService {

    void sendEmail(UserDto userDto);
}
