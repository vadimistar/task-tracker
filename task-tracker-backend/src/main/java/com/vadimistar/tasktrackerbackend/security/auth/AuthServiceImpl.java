package com.vadimistar.tasktrackerbackend.security.auth;

import com.vadimistar.tasktrackerbackend.email.SendEmailService;
import com.vadimistar.tasktrackerbackend.email.SendEmailTask;
import com.vadimistar.tasktrackerbackend.security.jwt.*;
import com.vadimistar.tasktrackerbackend.security.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SendEmailService sendEmailService;
    private final RegisterEmailConfig registerEmailConfig;

    @Override
    public JwtTokenDto loginUser(LoginUserDto loginUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.createToken(authentication);
    }

    @Override
    public JwtTokenDto registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = userMapper.mapRegisterUserDtoToUser(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        SendEmailTask sendEmailTask = SendEmailTask.builder()
                .destinationEmail(registerUserDto.getEmail())
                .header(registerEmailConfig.getSubject())
                .text(registerEmailConfig.getText())
                .build();
        sendEmailService.sendEmail(sendEmailTask);

        LoginUserDto loginUserDto = userMapper.mapRegisterUserDtoToAuthorizeUserDto(registerUserDto);
        return loginUser(loginUserDto);
    }
}
