package com.adefaultdev.DSDataAnalyzer.config;

import com.adefaultdev.DSDataAnalyzer.entity.User;
import com.adefaultdev.DSDataAnalyzer.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Class that initialize admin
 * Creates new User with Role "Admin" in database
 *
 * <p>Dependencies injected via Lombok's @RequiredArgsConstructor:</p>
 * <ul>
 *   <li>{@link UserRepository} - Handles user data persistence</li>
 *   <li>{@link PasswordEncoder} - Handles password encryption and validation</li>
 * </ul>
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    /**
     * Handles admin creation
     * Checking if admin already exist, creates it otherwise
     *
     * @param args command line arguments
     */
    @Override
    public void run(@Nullable String... args) {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole("ADMIN");

            userRepository.save(admin);
        }
    }
}
