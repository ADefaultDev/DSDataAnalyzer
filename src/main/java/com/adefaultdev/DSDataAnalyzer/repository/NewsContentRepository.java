package com.adefaultdev.DSDataAnalyzer.repository;

import com.adefaultdev.DSDataAnalyzer.entity.NewsContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for NewsContent managing.
 * Provides CRUD operations.
 * Extends default JpaRepository for NewsContent entity with id Long.
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
public interface NewsContentRepository extends JpaRepository<NewsContent, Long> {
    List<NewsContent> findBySiteId(Long siteId);
    List<NewsContent> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<NewsContent> findByThemeContainingIgnoreCase(String keyword);
    List<NewsContent> findByDateAfter(LocalDateTime fromDate);
}