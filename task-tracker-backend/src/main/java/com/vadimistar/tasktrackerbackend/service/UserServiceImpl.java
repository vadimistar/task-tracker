package com.vadimistar.tasktrackerbackend.service;

import com.vadimistar.tasktrackerbackend.dto.*;
import com.vadimistar.tasktrackerbackend.entity.User;
import com.vadimistar.tasktrackerbackend.exception.UserAlreadyExistsException;
import com.vadimistar.tasktrackerbackend.mapper.UserMapper;
import com.vadimistar.tasktrackerbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSendingService emailSendingService;

    @Override
    public JwtTokenDto registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = userMapper.mapRegisterUserDtoToUser(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        EmailSendingTaskDto emailSendingTask = EmailSendingTaskDto.builder()
                .destinationEmail(registerUserDto.getEmail())
                .header("Registration email")
                .text("Welcome to our service!")
                .build();
        emailSendingService.sendEmail(emailSendingTask);

        AuthorizeUserDto authorizeUserDto = userMapper.mapRegisterUserDtoToAuthorizeUserDto(registerUserDto);
        return authorizeUser(authorizeUserDto);
    }

    @Override
    public JwtTokenDto authorizeUser(AuthorizeUserDto authorizeUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authorizeUserDto.getEmail(), authorizeUserDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.createToken(authentication);
    }

    @Override
    public CurrentUserDto getCurrentUser(User user) {
        return userMapper.mapUserToCurrentUserDto(user);
    }
}
