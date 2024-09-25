package com.vadimistar.tasktrackerbackend;

import com.vadimistar.tasktrackerbackend.email.SendEmailService;
import com.vadimistar.tasktrackerbackend.email.SendEmailTask;
import com.vadimistar.tasktrackerbackend.security.auth.AuthService;
import com.vadimistar.tasktrackerbackend.security.auth.LoginUserDto;
import com.vadimistar.tasktrackerbackend.security.auth.RegisterUserDto;
import com.vadimistar.tasktrackerbackend.security.auth.UserAlreadyExistsException;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtService;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
import com.vadimistar.tasktrackerbackend.security.user.UserRepository;
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
public class AuthServiceTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private SendEmailService sendEmailService;

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

        JwtTokenDto jwtTokenDto = authService.registerUser(registerUserDto);
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

        authService.registerUser(registerUserDto);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(registerUserDto));
    }

    @Test
    void loginUser_validCredentials_returnsValidToken() {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        authService.registerUser(registerUserDto);

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        JwtTokenDto jwtTokenDto = authService.loginUser(loginUserDto);

        Assertions.assertTrue(jwtService.isTokenValid(jwtTokenDto.getToken()));
    }

    @Test
    void registerUser_success_registerEmailSent() {
        SendEmailTask sendEmailTask = SendEmailTask.builder()
                .destinationEmail("admin@admin.com")
                .subject("Registration email")
                .text("Welcome to our service!")
                .build();

        doNothing().when(sendEmailService).sendEmail(sendEmailTask);

        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        authService.registerUser(registerUserDto);
    }
}
