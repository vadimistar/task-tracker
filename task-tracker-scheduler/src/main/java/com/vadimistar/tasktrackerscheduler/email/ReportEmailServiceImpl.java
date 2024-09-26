package com.vadimistar.tasktrackerscheduler.email;

import com.vadimistar.tasktrackerscheduler.task.TaskDto;
import com.vadimistar.tasktrackerscheduler.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportEmailServiceImpl implements ReportEmailService {

    private final KafkaTemplate<String, SendEmailTask> kafkaTemplate;
    private final ReportEmailConfig reportEmailConfig;

    @Override
    public void sendEmail(UserDto userDto) {
        List<TaskDto> tasks = userDto.getTasks();

        String emailText = createNotCompletedTasksMessage(tasks).orElse("")
                + "\n"
                + createTodayCompletedTasksMessage(tasks).orElse("");

        if (emailText.isBlank()) {
            return;
        }

        SendEmailTask task = SendEmailTask.builder()
                .destinationEmail(userDto.getEmail())
                .subject(reportEmailConfig.getSubject())
                .text(emailText)
                .build();

        kafkaTemplate.send("EMAIL_SENDING_TASKS", task);
    }

    private Optional<String> createNotCompletedTasksMessage(List<TaskDto> tasks) {
        List<TaskDto> notCompletedTasks = tasks.stream()
                .filter(task -> !task.getIsCompleted())
                .toList();

        if (notCompletedTasks.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder result = new StringBuilder();
        result.append(String.format("You have %d not completed tasks:\n\n", tasks.size()));

        tasks.stream().limit(reportEmailConfig.getNotCompletedTasksLimit())
                .forEach(task -> result.append(" * ").append(task.getTitle()).append("\n"));

        return Optional.of(result.toString());
    }

    private Optional<String> createTodayCompletedTasksMessage(List<TaskDto> tasks) {
        List<TaskDto> tasksCompletedLastDay = getTasksCompletedLastDay(tasks);

        if (tasksCompletedLastDay.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder result = new StringBuilder();
        result.append(String.format("Today you have completed %d tasks:\n\n", tasks.size()));

        tasks.stream().limit(reportEmailConfig.getTodayCompletedTasksLimit())
                .forEach(task -> result.append(" * ").append(task.getTitle()).append("\n"));

        return Optional.of(result.toString());
    }

    private static List<TaskDto> getTasksCompletedLastDay(List<TaskDto> tasks) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayBefore = now.minusDays(1);

        return tasks.stream()
                .filter(task -> (task.getIsCompleted()) && task.getCompletedAt().isAfter(dayBefore))
                .toList();
    }
}
