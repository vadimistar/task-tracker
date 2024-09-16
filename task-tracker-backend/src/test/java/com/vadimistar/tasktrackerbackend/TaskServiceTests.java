package com.vadimistar.tasktrackerbackend;

import com.vadimistar.tasktrackerbackend.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerbackend.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.dto.UpdateTaskDto;
import com.vadimistar.tasktrackerbackend.entity.Task;
import com.vadimistar.tasktrackerbackend.entity.User;
import com.vadimistar.tasktrackerbackend.exception.TaskNotFoundException;
import com.vadimistar.tasktrackerbackend.repository.TaskRepository;
import com.vadimistar.tasktrackerbackend.repository.UserRepository;
import com.vadimistar.tasktrackerbackend.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
@Testcontainers
public class TaskServiceTests {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    public static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    }

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    public void getTasks_noTasks_returnsEmptyList() {
        List<TaskDto> tasks = taskService.getTasks(mockUser());

        Assertions.assertEquals(0, tasks.size());
    }

    @Test
    public void createTask_notCompleted_savesTaskInDatabase_notUpdatesCompletedAt() {
        CreateTaskDto request = CreateTaskDto.builder()
                .title("Some title")
                .text("Some text")
                .isCompleted(false)
                .build();

        TaskDto response = taskService.createTask(mockUser(), request);

        Assertions.assertTrue(taskRepository.existsById(response.getId()));

        Assertions.assertEquals("Some title", response.getTitle());
        Assertions.assertEquals("Some text", response.getText());
        Assertions.assertEquals(false, response.getIsCompleted());

        Assertions.assertNull(response.getCompletedAt());
    }

    @Test
    public void createTask_isCompleted_savesTaskInDatabase_updatesCompletedAt() {
        CreateTaskDto request = CreateTaskDto.builder()
                .title("Some title")
                .text("Some text")
                .isCompleted(true)
                .build();

        TaskDto response = taskService.createTask(mockUser(), request);

        Assertions.assertTrue(taskRepository.existsById(response.getId()));

        Assertions.assertEquals("Some title", response.getTitle());
        Assertions.assertEquals("Some text", response.getText());
        Assertions.assertEquals(true, response.getIsCompleted());

        Assertions.assertNotNull(response.getCompletedAt());
    }

    @Test
    public void createTask_titleNotTrimmed_trimsTitle() {
        CreateTaskDto request = CreateTaskDto.builder()
                .title("    Title   ")
                .text("Text")
                .build();

        TaskDto response = taskService.createTask(mockUser(), request);

        Assertions.assertEquals("Title", response.getTitle());
    }

    @Test
    public void updateTask_completeTask_updatesIsCompleted_completedAt() {
        User user = mockUser();
        Task task = mockTask(user, false);

        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(task.getId())
                .isCompleted(true)
                .build();

        TaskDto response = taskService.updateTask(user, request);

        Assertions.assertEquals(true, response.getIsCompleted());
        Assertions.assertNotNull(response.getCompletedAt());
    }

    @Test
    public void updateTask_makeTestNotCompleted_updatesIsCompleted_completedAt() {
        User user = mockUser();
        Task task = mockTask(user, true);

        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(task.getId())
                .isCompleted(false)
                .build();

        TaskDto response = taskService.updateTask(user, request);

        Assertions.assertEquals(false, response.getIsCompleted());
        Assertions.assertNull(response.getCompletedAt());
    }

    @Test
    public void updateTask_titleNotTrimmed_trimsTitle() {
        User user = mockUser();
        Task task = mockTask(user, false);

        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(task.getId())
                .title("    New title   ")
                .build();

        TaskDto response = taskService.updateTask(user, request);

        Assertions.assertEquals("New title", response.getTitle());
    }

    @Test
    public void updateTask_userNotOwnsTask_taskNotFound() {
        User user1 = mockUser();
        User user2 = mockUser2();
        Task taskOfUser1 = mockTask(user1, false);

        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(taskOfUser1.getId())
                .title("New title for task of user 1")
                .build();

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(user2, request));
    }

    @Test
    public void updateTask_invalidTaskId_taskNotFound() {
        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(Long.MAX_VALUE)
                .title("Title")
                .build();

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(mockUser(), request));
    }

    @Test
    public void deleteTask_taskExists_success() {
        User user = mockUser();
        Task task = mockTask(user, false);

        DeleteTaskDto request = DeleteTaskDto.builder()
                .id(task.getId())
                .build();

        Assertions.assertDoesNotThrow(() -> taskService.deleteTask(user, request));
    }

    @Test
    public void deleteTask_userNotOwnsTask_taskNotFound() {
        User user1 = mockUser();
        User user2 = mockUser2();
        Task taskOfUser1 = mockTask(user1, false);

        DeleteTaskDto request = DeleteTaskDto.builder()
                .id(taskOfUser1.getId())
                .build();

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(user2, request));
    }

    @Test
    public void deleteTask_invalidTaskId_taskNotFound() {
        DeleteTaskDto request = DeleteTaskDto.builder()
                .id(Long.MAX_VALUE)
                .build();

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(mockUser(), request));
    }

    private User mockUser() {
        return userRepository.saveAndFlush(
                User.builder()
                        .email("admin@admin.com")
                        .password("admin")
                        .build());
    }

    private User mockUser2() {
        return userRepository.saveAndFlush(
                User.builder()
                        .email("admin2@admin.com")
                        .password("admin")
                        .build());
    }

    private Task mockTask(User user, boolean isCompleted) {
        Task task = Task.builder()
                .title("Title")
                .text("Text")
                .isCompleted(isCompleted)
                .owner(user)
                .build();
        if (isCompleted) {
            task.setCompletedAt(LocalDateTime.now());
        }
        taskRepository.saveAndFlush(task);
        return task;
    }
}
