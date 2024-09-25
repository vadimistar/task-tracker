package com.vadimistar.tasktrackerbackend;

import com.vadimistar.tasktrackerbackend.security.*;
import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtService;
import com.vadimistar.tasktrackerbackend.security.details.UserDetailsServiceImpl;
import com.vadimistar.tasktrackerbackend.security.user.*;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("dev")
@Import({WebSecurityConfig.class, CorsConfig.class})
@Testcontainers
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtService jwtService;

    @Test
    void getCurrentUser_tokenCookieIsSet_returnsOk() throws Exception {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .email("admin@admin.com")
                .password("admin")
                .build();

        when(userDetailsService.loadUserByUsername("admin@admin.com"))
                .thenReturn(userDetails);

        CurrentUserDto response = CurrentUserDto.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .build();

        when(userService.getCurrentUser(userDetails)).thenReturn(response);

        when(jwtService.isTokenValid("TOKEN")).thenReturn(true);
        when(jwtService.getEmailFromToken("TOKEN")).thenReturn(userDetails.getEmail());

        mockMvc.perform(get("/api/user")
                        .cookie(new Cookie("authToken", "TOKEN")))
                .andExpect(status().isOk());
    }

    @Test
    void getCurrentUser_notAuthorized_returns401() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }
}
