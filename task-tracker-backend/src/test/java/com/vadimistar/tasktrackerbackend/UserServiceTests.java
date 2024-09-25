package com.vadimistar.tasktrackerbackend;

import com.vadimistar.tasktrackerbackend.email.EmailSendingService;
import com.vadimistar.tasktrackerbackend.email.EmailSendingTaskDto;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtService;
import com.vadimistar.tasktrackerbackend.security.user.*;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("dev")
@Testcontainers
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private EmailSendingService emailSendingService;

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
    }

    @Test
    void registerUser_validRequest_savesUserInDatabase_returnsValidToken() {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        JwtTokenDto jwtTokenDto = userService.registerUser(registerUserDto);
        String token = jwtTokenDto.getToken();

        Assertions.assertTrue(userRepository.existsByEmail("admin@admin.com"));

        Assertions.assertTrue(jwtService.isTokenValid(token));
        Assertions.assertEquals("admin@admin.com", jwtService.getEmailFromToken(token));
    }

    @Test
    void registerUser_emailAlreadyExists_throwsUserAlreadyExistsException() {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        userService.registerUser(registerUserDto);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(registerUserDto));
    }

    @Test
    void authorizeUser_validCredentials_returnsValidToken() {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        userService.registerUser(registerUserDto);

        AuthorizeUserDto authorizeUserDto = AuthorizeUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        JwtTokenDto jwtTokenDto = userService.authorizeUser(authorizeUserDto);

        Assertions.assertTrue(jwtService.isTokenValid(jwtTokenDto.getToken()));
    }

    @Test
    void registerUser_success_registerEmailSent() {
        EmailSendingTaskDto emailSendingTask = EmailSendingTaskDto.builder()
                .destinationEmail("admin@admin.com")
                .header("Registration email")
                .text("Welcome to our service!")
                .build();

        doNothing().when(emailSendingService).sendEmail(emailSendingTask);

        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        userService.registerUser(registerUserDto);
    }
}
