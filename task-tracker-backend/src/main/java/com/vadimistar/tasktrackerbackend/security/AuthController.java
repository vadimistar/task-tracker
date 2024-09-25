package com.vadimistar.tasktrackerbackend.security;

import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
import com.vadimistar.tasktrackerbackend.security.user.LoginUserDto;
import com.vadimistar.tasktrackerbackend.security.user.RegisterUserDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> loginUser(@Valid LoginUserDto loginUserDto,
                                       HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = authService.loginUser(loginUserDto);
        setTokenCookie(jwtTokenDto, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid RegisterUserDto registerUserDto,
                                          HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = authService.registerUser(registerUserDto);
        setTokenCookie(jwtTokenDto, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        setTokenCookie(JwtTokenDto.builder()
                .token("")
                .expiresIn(Duration.ZERO)
                .build(), response);
        return ResponseEntity.ok().build();
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
