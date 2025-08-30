package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.LoginRequest;
import com.adefaultdev.DSDataAnalyzer.dto.RegisterRequest;
import com.adefaultdev.DSDataAnalyzer.dto.UserResponse;
import com.adefaultdev.DSDataAnalyzer.entity.User;
import com.adefaultdev.DSDataAnalyzer.repository.UserRepository;
import com.adefaultdev.DSDataAnalyzer.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controller for user authentication and registration.
 * Provides APIs for user registration and login functionality.
 *
 * <p>Dependencies injected via Lombok's @RequiredArgsConstructor:</p>
 * <ul>
 *   <li>{@link UserRepository} - Handles user data persistence</li>
 *   <li>{@link PasswordEncoder} - Handles password encryption and validation</li>
 *   <li>{@link JwtTokenProvider} - Manages JWT token generation and validation</li>
 * </ul>
 *
 * @author ADefaultDev
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Handles user registration request.
     * Creates a new user account with the provided credentials.
     *
     * @param request RegisterRequest containing username and password
     * @return ResponseEntity with UserResponse on success or error message on failure
     */
    @Operation(
            summary = "New user's registration",
            requestBody = @RequestBody(
                    required = true,
                    description = "New user's credentials",
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "username": "new_user",
                                  "password": "password123",
                                }
                                """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successfull"),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        UserResponse response = toResponse(user);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles user login request.
     * Authenticates user credentials and returns a JWT token upon successful authentication.
     *
     * @param request LoginRequest containing username and password
     * @return ResponseEntity with JWT token on success or error message on failure
     */
    @Operation(
            summary = "New user's registration",
            requestBody = @RequestBody(
                    required = true,
                    description = "New user's credentials",
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "username": "admin",
                                  "password": "admin123",
                                }
                                """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successfull"),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
        }

        String token = jwtTokenProvider.generateToken(
                user.getUsername(),
                List.of(user.getRole())
        );

        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * Converts User entity to UserResponse DTO.
     *
     * @param user User entity object
     * @return UserResponse DTO containing user information
     */
    private UserResponse toResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }
}
