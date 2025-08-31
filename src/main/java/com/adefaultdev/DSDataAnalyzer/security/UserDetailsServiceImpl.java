package com.adefaultdev.DSDataAnalyzer.security;

import com.adefaultdev.DSDataAnalyzer.entity.User;
import com.adefaultdev.DSDataAnalyzer.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implementation for loading user details from database.
 * Bridges Spring Security with application's User entity.
 *
 * <p>Dependency injected via Lombok:</p>
 * <ul>
 *   <li>{@link UserRepository} - Handles user data access</li>
 * </ul>
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user by username from database.
     * Converts User entity to Spring Security UserDetails.
     *
     * @param username the username to search for
     * @return UserDetails implementation for Spring Security
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new UserDetailsImpl(user);
    }
}
