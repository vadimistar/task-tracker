package com.vadimistar.tasktrackerbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vadimistar.tasktrackerbackend.dto.ErrorDto;
import com.vadimistar.tasktrackerbackend.dto.RegisterUserDto;
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

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    public static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    }

    @Test
    void registerUser_success() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                        .email("admin@admin.com")
                        .password("admin")
                        .build();
        byte[] requestBody = objectMapper.writeValueAsBytes(registerUserDto);
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("authToken"));
    }

    @Test
    void registerUser_invalidEmail_returnsInvalidEmailError() throws Exception {
//        RegisterUserDto registerUserDto = RegisterUserDto.builder()
//                .email("admin")
//                .password("admin")
//                .build();
//        String requestBody = objectMapper.writeValueAsString(registerUserDto);
//
//        ErrorDto errorDto = ErrorDto.builder().message("Invalid email").build();
//        String responseBody = objectMapper.writeValueAsString(errorDto);
//
//        mockMvc.perform(post("/user")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(responseBody));
    }
}
