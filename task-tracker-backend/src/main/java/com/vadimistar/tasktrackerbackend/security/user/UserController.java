package com.vadimistar.tasktrackerbackend.security.user;

import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@Valid RegisterUserDto registerUserDto,
                                          HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = userService.registerUser(registerUserDto);
        setTokenCookie(jwtTokenDto, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public CurrentUserDto getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getCurrentUser(userDetails);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> authorizeUser(@Valid AuthorizeUserDto authorizeUserDto,
                                                HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = userService.authorizeUser(authorizeUserDto);
        setTokenCookie(jwtTokenDto, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/logout")
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
