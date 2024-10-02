package com.vadimistar.tasktrackerapi.security.user;

import com.vadimistar.tasktrackerapi.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public CurrentUserDto getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getCurrentUser(userDetails);
    }
}
