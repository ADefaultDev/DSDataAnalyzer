package com.adefaultdev.DSDataAnalyzer.repository;

import com.adefaultdev.DSDataAnalyzer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for NewsContent managing.
 * Provides CRUD operations.
 * Extends default JpaRepository for NewsContent entity with id Long.
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
public interface UserRepository extends JpaRepository<User, Long>  {
    Optional<User> findByUsername(String username);
}
