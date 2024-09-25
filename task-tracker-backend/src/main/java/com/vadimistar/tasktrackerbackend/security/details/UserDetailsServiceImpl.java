package com.vadimistar.tasktrackerbackend.security.details;

import com.vadimistar.tasktrackerbackend.security.user.User;
import com.vadimistar.tasktrackerbackend.security.user.UserMapper;
import com.vadimistar.tasktrackerbackend.security.user.UserRepository;
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
