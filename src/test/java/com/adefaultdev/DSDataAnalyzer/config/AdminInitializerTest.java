package com.adefaultdev.DSDataAnalyzer.config;

import com.adefaultdev.DSDataAnalyzer.controller.AuthController;
import com.adefaultdev.DSDataAnalyzer.dto.RegisterRequest;
import com.adefaultdev.DSDataAnalyzer.entity.User;
import com.adefaultdev.DSDataAnalyzer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AdminInitializer}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class AdminInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AdminInitializer adminInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminInitializer = new AdminInitializer(userRepository, passwordEncoder);

        ReflectionTestUtils.setField(adminInitializer, "adminUsername", "admin");
        ReflectionTestUtils.setField(adminInitializer, "adminPassword", "password");
    }

    /**
     * Tests {@link AdminInitializer#run(String...)} if admin not exists.
     * Verifies that admin created and saved in DB.
     */
    @Test
    void shouldCreateAdminIfNotExists() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        adminInitializer.run();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("admin", saved.getUsername());
        assertEquals("encodedPassword", saved.getPassword());
        assertEquals("ADMIN", saved.getRole());
    }

    /**
     * Tests {@link AdminInitializer#run(String...)} if admin exists.
     * Verifies that new admin is not created if already exists.
     */
    @Test
    void shouldNotCreateAdminIfAlreadyExists() {
        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(new User()));

        adminInitializer.run();

        verify(userRepository, never()).save(any(User.class));
    }
}
