package com.library.library.controllers;

import com.library.library.config.JwtUtils;
import com.library.library.dtos.AuthResponseDTO;
import com.library.library.dtos.ChangePasswordDTO;
import com.library.library.dtos.LoginUser;
import com.library.library.dtos.RegisterUserDTO;
import com.library.library.enums.RoleEnum;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.models.UserEntity;
import com.library.library.services.AuthService;
import com.library.library.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;


    @Operation(summary = "Login user", description = "Authenticate a user and generate a JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Invalid email or password"))),
            @ApiResponse(responseCode = "500", description = "Internal server error during login",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "An error occurred during login")))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginUser loginRequest) {
        return authService.authenticateUser(loginRequest);
    }


    @Operation(summary = "Register user", description = "Create a new user and generate a JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid fields",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Every field is required."))),
            @ApiResponse(responseCode = "400", description = "Email or username already in use",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "The email is already in use.")))
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        AuthResponseDTO response = authService.registerUser(registerUserDTO);
        if (response.getMessage().equals("Every field is required.") || response.getMessage().equals("The email is already in use.")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Change password", description = "Allow the user to change their password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid current password or new password too short",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "New password must be at least 8 characters long"))),
            @ApiResponse(responseCode = "401", description = "Authorization token is missing or invalid",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Authorization token is missing or invalid"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "User not found"))),
            @ApiResponse(responseCode = "400", description = "New password cannot be the same as the current password",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "New password cannot be the same as the current password")))
    })
    @PutMapping("/change-password")
    public ResponseEntity<AuthResponseDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, Authentication authentication) throws UserNotFoundException {
        AuthResponseDTO response = userService.changePassword(changePasswordDTO, authentication);
        if (response.getMessage().equals("Password updated successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
