package com.library.library.services.implementations;

import com.library.library.config.JwtUtils;
import com.library.library.dtos.AuthResponseDTO;
import com.library.library.dtos.LoginUser;
import com.library.library.dtos.RegisterUserDTO;
import com.library.library.enums.RoleEnum;
import com.library.library.models.UserEntity;
import com.library.library.repositories.UserRepository;
import com.library.library.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<AuthResponseDTO> authenticateUser(LoginUser loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateToken(authentication.getName());
            return ResponseEntity.ok(new AuthResponseDTO(jwt, "Logged in successfully"));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDTO("-", "Invalid email or password"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponseDTO("-", "An error occurred during login"));
        }
    }


    @Override
    public AuthResponseDTO registerUser(RegisterUserDTO registerUserDTO) {
        if (registerUserDTO.getEmail() == null || registerUserDTO.getPassword() == null) {
            return new AuthResponseDTO("-", "Every field is required.");
        }

        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            return new AuthResponseDTO("-", "The email or username is already in use.");
        }

        UserEntity user = new UserEntity();
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setRole(RoleEnum.USER);

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());

        return new AuthResponseDTO(token, "User registered successfully");
    }
}