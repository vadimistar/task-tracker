package com.vadimistar.tasktrackerbackend;

import com.vadimistar.tasktrackerbackend.dto.CreateTaskDto;
import com.vadimistar.tasktrackerbackend.dto.DeleteTaskDto;
import com.vadimistar.tasktrackerbackend.dto.TaskDto;
import com.vadimistar.tasktrackerbackend.dto.UpdateTaskDto;
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
    public void createTask_notCompleted() {
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Some title")
                .text("Some text")
                .isCompleted(false)
                .build();
        TaskDto createdTask = taskService.createTask(mockUser(), createTaskDto);
        Assertions.assertEquals("Some title", createdTask.getTitle());
        Assertions.assertEquals("Some text", createdTask.getText());
        Assertions.assertEquals(false, createdTask.getIsCompleted());
        Assertions.assertNull(createdTask.getCompletedAt());
    }

    @Test
    public void createTask_isCompleted() {
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Some title")
                .text("Some text")
                .isCompleted(true)
                .build();
        TaskDto createdTask = taskService.createTask(mockUser(), createTaskDto);
        Assertions.assertEquals("Some title", createdTask.getTitle());
        Assertions.assertEquals("Some text", createdTask.getText());
        Assertions.assertEquals(true, createdTask.getIsCompleted());
        Assertions.assertNotNull(createdTask.getCompletedAt());
    }

    @Test
    public void createTask_titleNotTrimmed_trimsTitle() {
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("    Title   ")
                .text("Text")
                .build();
        TaskDto createdTask = taskService.createTask(mockUser(), createTaskDto);
        Assertions.assertEquals("Title", createdTask.getTitle());
    }

    @Test
    public void createTask_titleIsEmpty_throwsException() {
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("")
                .text("Text")
                .build();
        Assertions.assertThrows(Exception.class, () -> taskService.createTask(mockUser(), createTaskDto));
    }

    @Test
    public void updateTask_completeTask() {
        User user = mockUser();
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Title")
                .isCompleted(false)
                .build();
        TaskDto task = taskService.createTask(user, createTaskDto);
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .id(task.getId())
                .isCompleted(true)
                .build();
        task = taskService.updateTask(user, updateTaskDto);
        Assertions.assertEquals(true, task.getIsCompleted());
        Assertions.assertNotNull(task.getCompletedAt());
    }

    @Test
    public void updateTask_incompleteTask() {
        User user = mockUser();
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Title")
                .isCompleted(true)
                .build();
        TaskDto task = taskService.createTask(user, createTaskDto);
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .id(task.getId())
                .isCompleted(false)
                .build();
        task = taskService.updateTask(user, updateTaskDto);
        Assertions.assertEquals(false, task.getIsCompleted());
        Assertions.assertNull(task.getCompletedAt());
    }

    @Test
    public void updateTask_titleNotTrimmed_trimsTitle() {
        User user = mockUser();
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Title")
                .build();
        TaskDto task = taskService.createTask(user, createTaskDto);
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .id(task.getId())
                .title("    New title   ")
                .build();
        task = taskService.updateTask(user, updateTaskDto);
        Assertions.assertEquals("New title", task.getTitle());
    }

    @Test
    public void updateTask_titleIsEmpty_throwsException() {
        User user = mockUser();
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Title")
                .build();
        TaskDto task = taskService.createTask(user, createTaskDto);
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .id(task.getId())
                .title("")
                .build();
        Assertions.assertThrows(Exception.class, () -> taskService.updateTask(user, updateTaskDto));
    }

    @Test
    public void updateTask_userNotOwnsTask_taskNotFound() {
        User user1 = mockUser();
        User user2 = mockUser2();
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Task of user 1")
                .build();
        TaskDto task = taskService.createTask(user1, createTaskDto);
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .id(task.getId())
                .title("New title for task of user 1")
                .build();
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(user2, updateTaskDto));
    }

    @Test
    public void updateTask_invalidTaskId_taskNotFound() {
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .id(Long.MAX_VALUE)
                .title("Title")
                .build();
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(mockUser(), updateTaskDto));
    }

    @Test
    public void deleteTask_taskExists() {
        User user = mockUser();
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Title")
                .build();
        TaskDto task = taskService.createTask(user, createTaskDto);
        DeleteTaskDto deleteTaskDto = DeleteTaskDto.builder()
                .id(task.getId())
                .build();
        taskService.deleteTask(user, deleteTaskDto);
    }

    @Test
    public void deleteTask_userNotOwnsTask_taskNotFound() {
        User user1 = mockUser();
        User user2 = mockUser2();
        CreateTaskDto createTaskDto = CreateTaskDto.builder()
                .title("Task of user 1")
                .build();
        TaskDto task = taskService.createTask(user1, createTaskDto);
        DeleteTaskDto deleteTaskDto = DeleteTaskDto.builder()
                .id(task.getId())
                .build();
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(user2, deleteTaskDto));
    }

    @Test
    public void deleteTask_invalidTaskId_taskNotFound() {
        DeleteTaskDto deleteTaskDto = DeleteTaskDto.builder()
                .id(Long.MAX_VALUE)
                .build();
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(mockUser(), deleteTaskDto));
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
}
