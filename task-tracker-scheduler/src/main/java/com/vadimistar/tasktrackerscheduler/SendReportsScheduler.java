package com.vadimistar.tasktrackerscheduler;

import com.vadimistar.tasktrackerscheduler.email.ReportEmailService;
import com.vadimistar.tasktrackerscheduler.user.UserDto;
import com.vadimistar.tasktrackerscheduler.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class SendReportsScheduler {

    private final UserService userService;
    private final ReportEmailService reportEmailService;

    @PostConstruct
    public void onStartup() {
        sendReports();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendReports() {
        List<UserDto> users = userService.getAllUsers();

        for (UserDto user : users) {
            reportEmailService.sendEmail(user);
        }
    }

}
