package com.library.library.config;

import com.library.library.exceptions.UserNotFoundException;
import com.library.library.models.UserEntity;
import com.library.library.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    public UserEntity getAuthenticatedUser(Authentication authentication) throws UserNotFoundException {
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + username));
    }
}
