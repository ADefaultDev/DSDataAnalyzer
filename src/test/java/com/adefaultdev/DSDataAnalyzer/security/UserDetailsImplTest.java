package com.adefaultdev.DSDataAnalyzer.security;

import com.adefaultdev.DSDataAnalyzer.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link UserDetailsImplTest}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class UserDetailsImplTest {

    @Test
    void getAuthorities_shouldReturnRoleWithPrefix() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("secret");
        user.setRole("ADMIN");

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        assertEquals(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")), userDetails.getAuthorities());
    }

    @Test
    void getUsername_shouldReturnUserUsername() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("pwd");
        user.setRole("USER");

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        assertEquals("john", userDetails.getUsername());
    }

    @Test
    void getPassword_shouldReturnUserPassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("pwd123");
        user.setRole("USER");

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        assertEquals("pwd123", userDetails.getPassword());
    }

    @Test
    void accountFlags_shouldAlwaysBeTrue() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("pwd123");
        user.setRole("USER");

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}
