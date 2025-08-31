package com.adefaultdev.DSDataAnalyzer.repository;

import com.adefaultdev.DSDataAnalyzer.entity.NewsSite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for NewsSite managing.
 * Provides CRUD operations.
 * Extends default JpaRepository for NewsSite entity with id Long.
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
public interface NewsSiteRepository extends JpaRepository<NewsSite, Long> {
    Optional<NewsSite> findByAddress(String address);
    boolean existsByAddress(String address);
    Optional<NewsSite> findByName(String sourceName);
}