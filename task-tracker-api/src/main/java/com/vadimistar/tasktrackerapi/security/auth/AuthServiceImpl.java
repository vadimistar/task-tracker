package com.vadimistar.tasktrackerapi.security.auth;

import com.vadimistar.tasktrackerapi.email.RegisterEmailService;
import com.vadimistar.tasktrackerapi.security.jwt.*;
import com.vadimistar.tasktrackerapi.security.user.*;
import jakarta.transaction.Transactional;
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
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final RegisterEmailService registerEmailService;

    @Override
    public JwtTokenDto loginUser(LoginUserDto loginUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.createToken(authentication);
    }

    @Override
    @Transactional
    public JwtTokenDto registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = authMapper.mapRegisterUserDtoToUser(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);

        registerEmailService.sendEmail(registerUserDto);

        LoginUserDto loginUserDto = authMapper.mapRegisterUserDtoToLoginUserDto(registerUserDto);
        return loginUser(loginUserDto);
    }
}
