package com.adefaultdev.DSDataAnalyzer.security;

import com.adefaultdev.DSDataAnalyzer.entity.User;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 * Spring Security UserDetails implementation wrapping the User entity.
 * Adapts the application's User entity to Spring Security's requirements.
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
public record UserDetailsImpl(User user) implements UserDetails {

    /**
     * Returns the authorities granted to the user.
     * Converts the user's role to Spring Security GrantedAuthority.
     *
     * @return Collection of granted authorities
     */
    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @NonNull
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
