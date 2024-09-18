package com.vadimistar.tasktrackerbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vadimistar.tasktrackerbackend.config.WebSecurityConfig;
import com.vadimistar.tasktrackerbackend.controller.UserController;
import com.vadimistar.tasktrackerbackend.dto.*;
import com.vadimistar.tasktrackerbackend.entity.User;
import com.vadimistar.tasktrackerbackend.service.JwtService;
import com.vadimistar.tasktrackerbackend.service.UserDetailsServiceImpl;
import com.vadimistar.tasktrackerbackend.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("dev")
@Import(WebSecurityConfig.class)
@Testcontainers
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtService jwtService;

    @Test
    void registerUser_validRequest_returnsOk_setsAuthCookie() throws Exception {
        RegisterUserDto request = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        when(userService.registerUser(request)).thenReturn(mockJwtTokenDto());

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("authToken"));
    }

    @Test
    void authorizeUser_validRequest_returnsOk_setsAuthCookie() throws Exception {
        AuthorizeUserDto request = AuthorizeUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        when(userService.authorizeUser(request)).thenReturn(mockJwtTokenDto());

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("authToken"));
    }

    @Test
    void getCurrentUser_tokenCookieIsSet_returnsOk() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("admin@admin.com")
                .password("admin")
                .build();

        when(userDetailsService.loadUserByUsername("admin@admin.com"))
                .thenReturn(user);

        CurrentUserDto response = CurrentUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();

        when(userService.getCurrentUser(user)).thenReturn(response);

        when(jwtService.isTokenValid("TOKEN")).thenReturn(true);
        when(jwtService.getEmailFromToken("TOKEN")).thenReturn(user.getEmail());

        mockMvc.perform(get("/api/user")
                        .cookie(new Cookie("authToken", "TOKEN")))
                .andExpect(status().isOk());
    }

    @Test
    void getCurrentUser_notAuthorized_returns401() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }

    private JwtTokenDto mockJwtTokenDto() {
        return JwtTokenDto.builder()
                .token("TOKEN")
                .expiresIn(Duration.ofSeconds(60))
                .build();
    }
}
