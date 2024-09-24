package com.vadimistar.tasktrackerscheduler.service;

import com.vadimistar.tasktrackerscheduler.dto.EmailSendingTaskDto;
import com.vadimistar.tasktrackerscheduler.dto.TaskDto;
import com.vadimistar.tasktrackerscheduler.dto.UserDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class SendReportsService {

    private final UserService userService;
    private final TaskService taskService;
    private final EmailSendingService emailSendingService;

    @PostConstruct
    public void onStartup() {
        sendReports();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendReports() {
        List<UserDto> users = userService.getAllUsers();

        for (UserDto user : users) {
            List<TaskDto> tasks = user.getTasks();

            if (tasks.isEmpty()) {
                continue;
            }

            StringBuilder emailText = new StringBuilder();

            List<TaskDto> notCompletedTasks = tasks.stream()
                    .filter(task -> !task.getIsCompleted())
                    .toList();

            if (!notCompletedTasks.isEmpty()) {
                emailText.append(createNotCompletedTasksMessage(notCompletedTasks));
            }

            List<TaskDto> tasksCompletedLastDay = taskService.getTasksCompletedLastDay(tasks);

            if (!tasksCompletedLastDay.isEmpty()) {
                if (!emailText.isEmpty()) {
                    emailText.append("\n");
                }

                emailText.append(createCompletedTasksMessage(tasksCompletedLastDay));
            }

            EmailSendingTaskDto emailSendingTask = EmailSendingTaskDto.builder()
                    .destinationEmail(user.getEmail())
                    .header("Task Service - Daily Report")
                    .text(emailText.toString())
                    .build();

            emailSendingService.sendEmail(emailSendingTask);
        }
    }

    private String createNotCompletedTasksMessage(List<TaskDto> tasks) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("You have %d not completed tasks:\n\n", tasks.size()));

        tasks.stream().limit(NOT_COMPLETED_TASKS_LIMIT).forEach(task -> {
            result.append(" * ").append(task.getTitle()).append("\n");
        });

        return result.toString();
    }

    private String createCompletedTasksMessage(List<TaskDto> tasks) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Today you have completed %d tasks:\n\n", tasks.size()));

        tasks.stream().limit(COMPLETED_TASKS_LIMIT).forEach(task -> {
            result.append(" * ").append(task.getTitle()).append("\n");
        });

        return result.toString();
    }

    private static final int NOT_COMPLETED_TASKS_LIMIT = 5;
    private static final int COMPLETED_TASKS_LIMIT = 10;

}
