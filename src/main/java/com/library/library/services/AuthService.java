package com.library.library.services;

import com.library.library.dtos.AuthResponseDTO;
import com.library.library.dtos.LoginUser;
import com.library.library.dtos.RegisterUserDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<AuthResponseDTO> authenticateUser(LoginUser loginRequest);

    AuthResponseDTO registerUser(RegisterUserDTO registerUserDTO);

}
