package com.vadimistar.tasktrackerbackend.service;

import com.vadimistar.tasktrackerbackend.entity.User;
import com.vadimistar.tasktrackerbackend.mapper.UserMapper;
import com.vadimistar.tasktrackerbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with this email is not found: " + email));
        return userMapper.mapUserToUserDetailsImpl(user);
    }
}
