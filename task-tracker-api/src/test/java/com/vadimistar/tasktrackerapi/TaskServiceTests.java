package com.vadimistar.tasktrackerapi;

import com.vadimistar.tasktrackerapi.security.details.UserDetailsMapper;
import com.vadimistar.tasktrackerapi.task.dto.CreateTaskDto;
import com.vadimistar.tasktrackerapi.task.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerapi.task.dto.TaskDto;
import com.vadimistar.tasktrackerapi.task.dto.UpdateTaskDto;
import com.vadimistar.tasktrackerapi.task.Task;
import com.vadimistar.tasktrackerapi.security.user.User;
import com.vadimistar.tasktrackerapi.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerapi.task.TaskNotFoundException;
import com.vadimistar.tasktrackerapi.task.TaskRepository;
import com.vadimistar.tasktrackerapi.security.user.UserRepository;
import com.vadimistar.tasktrackerapi.task.TaskService;
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

    @Autowired
    private UserDetailsMapper userDetailsMapper;

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
        List<TaskDto> tasks = taskService.getTasks(mockUserDetails());

        Assertions.assertEquals(0, tasks.size());
    }

    @Test
    public void createTask_notCompleted_savesTaskInDatabase_notUpdatesCompletedAt() {
        CreateTaskDto request = CreateTaskDto.builder()
                .title("Some title")
                .text("Some text")
                .isCompleted(false)
                .build();

        TaskDto response = taskService.createTask(mockUserDetails(), request);

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

        TaskDto response = taskService.createTask(mockUserDetails(), request);

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

        TaskDto response = taskService.createTask(mockUserDetails(), request);

        Assertions.assertEquals("Title", response.getTitle());
    }

    @Test
    public void updateTask_completeTask_updatesIsCompleted_completedAt() {
        UserDetailsImpl userDetails = mockUserDetails();
        Task task = mockTask(userDetails, false);

        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(task.getId())
                .isCompleted(true)
                .build();

        TaskDto response = taskService.updateTask(userDetails, request);

        Assertions.assertEquals(true, response.getIsCompleted());
        Assertions.assertNotNull(response.getCompletedAt());
    }

    @Test
    public void updateTask_makeTestNotCompleted_updatesIsCompleted_completedAt() {
        UserDetailsImpl userDetails = mockUserDetails();
        Task task = mockTask(userDetails, true);

        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(task.getId())
                .isCompleted(false)
                .build();

        TaskDto response = taskService.updateTask(userDetails, request);

        Assertions.assertEquals(false, response.getIsCompleted());
        Assertions.assertNull(response.getCompletedAt());
    }

    @Test
    public void updateTask_titleNotTrimmed_trimsTitle() {
        UserDetailsImpl userDetails = mockUserDetails();
        Task task = mockTask(userDetails, false);

        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(task.getId())
                .title("    New title   ")
                .build();

        TaskDto response = taskService.updateTask(userDetails, request);

        Assertions.assertEquals("New title", response.getTitle());
    }

    @Test
    public void updateTask_userNotOwnsTask_taskNotFound() {
        UserDetailsImpl userDetails1 = mockUserDetails();
        UserDetailsImpl userDetails2 = mockUserDetails2();
        Task taskOfUser1 = mockTask(userDetails1, false);

        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(taskOfUser1.getId())
                .title("New title for task of user 1")
                .build();

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(userDetails2, request));
    }

    @Test
    public void updateTask_invalidTaskId_taskNotFound() {
        UpdateTaskDto request = UpdateTaskDto.builder()
                .id(Long.MAX_VALUE)
                .title("Title")
                .build();

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(mockUserDetails(), request));
    }

    @Test
    public void deleteTask_taskExists_success() {
        UserDetailsImpl userDetails = mockUserDetails();
        Task task = mockTask(userDetails, false);

        DeleteTaskDto request = DeleteTaskDto.builder()
                .id(task.getId())
                .build();

        Assertions.assertDoesNotThrow(() -> taskService.deleteTask(userDetails, request));
    }

    @Test
    public void deleteTask_userNotOwnsTask_taskNotFound() {
        UserDetailsImpl userDetails1 = mockUserDetails();
        UserDetailsImpl userDetails2 = mockUserDetails2();
        Task taskOfUser1 = mockTask(userDetails1, false);

        DeleteTaskDto request = DeleteTaskDto.builder()
                .id(taskOfUser1.getId())
                .build();

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(userDetails2, request));
    }

    @Test
    public void deleteTask_invalidTaskId_taskNotFound() {
        DeleteTaskDto request = DeleteTaskDto.builder()
                .id(Long.MAX_VALUE)
                .build();

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(mockUserDetails(), request));
    }

    private UserDetailsImpl mockUserDetails() {
        User user = userRepository.saveAndFlush(
                User.builder()
                        .email("admin@admin.com")
                        .password("admin")
                        .build());
        return userDetailsMapper.mapUserToUserDetailsImpl(user);
    }

    private UserDetailsImpl mockUserDetails2() {
        User user = userRepository.saveAndFlush(
                User.builder()
                        .email("admin2@admin.com")
                        .password("admin")
                        .build());
        return userDetailsMapper.mapUserToUserDetailsImpl(user);
    }

    private Task mockTask(UserDetailsImpl userDetails, boolean isCompleted) {
        User owner = userRepository.findById(userDetails.getId()).get();
        Task task = Task.builder()
                .title("Title")
                .text("Text")
                .isCompleted(isCompleted)
                .owner(owner)
                .build();
        if (isCompleted) {
            task.setCompletedAt(LocalDateTime.now());
        }
        taskRepository.saveAndFlush(task);
        return task;
    }
}
