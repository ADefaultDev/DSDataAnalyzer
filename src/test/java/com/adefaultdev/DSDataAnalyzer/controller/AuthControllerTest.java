package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.LoginRequest;
import com.adefaultdev.DSDataAnalyzer.dto.RegisterRequest;
import com.adefaultdev.DSDataAnalyzer.dto.UserResponse;
import com.adefaultdev.DSDataAnalyzer.entity.User;
import com.adefaultdev.DSDataAnalyzer.repository.UserRepository;
import com.adefaultdev.DSDataAnalyzer.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link AuthController}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class AuthControllerTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        authController = new AuthController(userRepository, passwordEncoder, jwtTokenProvider);
    }

    /**
     * Tests {@link AuthController#register(RegisterRequest)} with a new username.
     * Verifies that a user is saved and response contains user data.
     */
    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setPassword("password");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPass");

        ResponseEntity<?> response = authController.register(request);

        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(UserResponse.class, response.getBody());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("newUser", savedUser.getUsername());
        assertEquals("encodedPass", savedUser.getPassword());
        assertEquals("USER", savedUser.getRole());
    }

    /**
     * Tests {@link AuthController#register(RegisterRequest)} when username already exists.
     * Verifies that an error message is returned.
     */
    @Test
    void testRegister_UsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existingUser");
        request.setPassword("password");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).containsKey("error"));
        verify(userRepository, never()).save(any());
    }

    /**
     * Tests {@link AuthController#login(LoginRequest)} with valid credentials.
     * Verifies that a JWT token is returned.
     */
    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("validUser");
        request.setPassword("password");

        User user = new User();
        user.setUsername("validUser");
        user.setPassword("encodedPass");
        user.setRole("USER");

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPass")).thenReturn(true);
        when(jwtTokenProvider.generateToken("validUser", List.of("USER")))
                .thenReturn("mocked-jwt-token");

        ResponseEntity<?> response = authController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).containsKey("token"));
        assertEquals("mocked-jwt-token", ((Map<?, ?>) response.getBody()).get("token"));
    }

    /**
     * Tests {@link AuthController#login(LoginRequest)} with invalid password.
     * Verifies that an error message is returned.
     */
    @Test
    void testLogin_InvalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("validUser");
        request.setPassword("wrongPass");

        User user = new User();
        user.setUsername("validUser");
        user.setPassword("encodedPass");

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        ResponseEntity<?> response = authController.login(request);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).containsKey("error"));
    }

    /**
     * Tests {@link AuthController#login(LoginRequest)} when user does not exist.
     * Verifies that a RuntimeException is thrown.
     */
    @Test
    void testLogin_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("notFound");
        request.setPassword("password");

        when(userRepository.findByUsername("notFound")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authController.login(request));
    }
}
