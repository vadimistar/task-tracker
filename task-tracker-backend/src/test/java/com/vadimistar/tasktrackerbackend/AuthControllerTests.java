package com.vadimistar.tasktrackerbackend;

import com.vadimistar.tasktrackerbackend.security.*;
import com.vadimistar.tasktrackerbackend.security.details.UserDetailsServiceImpl;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtConfig;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtService;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtServiceImpl;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
import com.vadimistar.tasktrackerbackend.security.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ActiveProfiles("dev")
@Import(WebSecurityConfig.class)
@MockBean(classes = {CorsConfig.class, JwtService.class, UserDetailsService.class})
@Testcontainers
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void registerUser_validRequest_returnsOk_setsAuthCookie() throws Exception {
        RegisterUserDto request = RegisterUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        when(authService.registerUser(request)).thenReturn(mockJwtTokenDto());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("email", request.getEmail())
                        .param("password", request.getPassword()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("authToken"));
    }

    @Test
    void authorizeUser_validRequest_returnsOk_setsAuthCookie() throws Exception {
        AuthorizeUserDto request = AuthorizeUserDto.builder()
                .email("admin@admin.com")
                .password("admin")
                .build();

        when(authService.authorizeUser(request)).thenReturn(mockJwtTokenDto());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("email", request.getEmail())
                        .param("password", request.getPassword()))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("authToken"));
    }

    private JwtTokenDto mockJwtTokenDto() {
        return JwtTokenDto.builder()
                .token("TOKEN")
                .expiresIn(Duration.ofSeconds(60))
                .build();
    }
}
