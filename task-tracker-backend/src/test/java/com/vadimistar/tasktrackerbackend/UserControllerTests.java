package com.vadimistar.tasktrackerbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vadimistar.tasktrackerbackend.dto.*;
import com.vadimistar.tasktrackerbackend.repository.UserRepository;
import com.vadimistar.tasktrackerbackend.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Testcontainers
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

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
    }

    @Test
    void registerUser_success() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                        .email("admin@admin.com")
                        .password("admin")
                        .build();
        String requestBody = objectMapper.writeValueAsString(registerUserDto);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("authToken"));
    }

    @Test
    void registerUser_invalidEmail_returnsInvalidEmailError() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin")
                .password("admin")
                .build();
        String requestBody = objectMapper.writeValueAsString(registerUserDto);

        ErrorDto errorDto = new ErrorDto("Email is invalid");
        String responseBody = objectMapper.writeValueAsString(errorDto);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseBody));
    }

    @Test
    void registerUser_emailAlreadyExists_returnsEmailExistsError() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        String requestBody = objectMapper.writeValueAsString(registerUserDto);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        ErrorDto errorDto = new ErrorDto("User with this email already exists");
        String responseBody = objectMapper.writeValueAsString(errorDto);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(content().json(responseBody));
    }

    @Test
    void registerUser_passwordTooSmall_returnsPasswordTooSmallError() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("1")
                .build();
        String requestBody = objectMapper.writeValueAsString(registerUserDto);

        ErrorDto errorDto = new ErrorDto("Password must be at least 5 characters");
        String responseBody = objectMapper.writeValueAsString(errorDto);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseBody));
    }

    @Test
    void authorizeUser_success() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        userService.registerUser(registerUserDto);

        AuthorizeUserDto authorizeUserDto = AuthorizeUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        String requestBody = objectMapper.writeValueAsString(authorizeUserDto);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("authToken"));
    }

    @Test
    void authorizeUser_invalidCredentials_returnsInvalidCredentialsError() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        userService.registerUser(registerUserDto);

        AuthorizeUserDto authorizeUserDto = AuthorizeUserDto.builder()
                .email("admin@admin.com")
                .password("123123")
                .build();
        String requestBody = objectMapper.writeValueAsString(authorizeUserDto);

        ErrorDto errorDto = new ErrorDto("Bad credentials");
        String responseBody = objectMapper.writeValueAsString(errorDto);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseBody));
    }

    @Test
    void getCurrentUser_registerAndGetCurrentUser_success() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();
        JwtTokenDto jwtTokenDto = userService.registerUser(registerUserDto);

        CurrentUserDto currentUserDto = CurrentUserDto.builder()
                .id(1L)
                .email("admin@admin.com")
                .build();
        String responseBody = objectMapper.writeValueAsString(currentUserDto);

        mockMvc.perform(get("/user")
                        .cookie(new Cookie("authToken", jwtTokenDto.getToken())))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    void invalidRequestDto_returnsInvalidRequestFormatError() throws Exception {
        ErrorDto errorDto = new ErrorDto("Invalid request format");
        String responseBody = objectMapper.writeValueAsString(errorDto);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_ATOM_XML)
                        .content("bad content"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseBody));
    }
}
