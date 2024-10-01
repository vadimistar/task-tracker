package com.vadimistar.tasktrackerbackend.security.auth;

import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public void loginUser(@Valid LoginUserDto loginUserDto,
                                       HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = authService.loginUser(loginUserDto);
        setTokenCookie(jwtTokenDto, response);
    }

    @PostMapping("/register")
    public void registerUser(@Valid RegisterUserDto registerUserDto,
                                          HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = authService.registerUser(registerUserDto);
        setTokenCookie(jwtTokenDto, response);
    }

    @PostMapping("/logout")
    public void logoutUser(HttpServletResponse response) {
        setTokenCookie(JwtTokenDto.builder()
                .token("")
                .expiresIn(Duration.ZERO)
                .build(), response);
    }

    private static void setTokenCookie(JwtTokenDto jwtTokenDto, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie
                .from("authToken", jwtTokenDto.getToken())
                .httpOnly(true)
                .path("/")
                .maxAge(jwtTokenDto.getExpiresIn())
                .build();
        response.addHeader("Set-Cookie", responseCookie.toString());
    }
}
