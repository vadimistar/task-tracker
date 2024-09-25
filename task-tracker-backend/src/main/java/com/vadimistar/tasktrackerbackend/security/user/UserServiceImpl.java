package com.vadimistar.tasktrackerbackend.security.user;

import com.vadimistar.tasktrackerbackend.email.EmailSendingService;
import com.vadimistar.tasktrackerbackend.email.EmailSendingTaskDto;
import com.vadimistar.tasktrackerbackend.security.details.UserDetailsImpl;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtService;
import com.vadimistar.tasktrackerbackend.security.jwt.JwtTokenDto;
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
    public CurrentUserDto getCurrentUser(UserDetailsImpl userDetails) {
        return userMapper.mapUserDetailsImplToCurrentUserDto(userDetails);
    }
}
