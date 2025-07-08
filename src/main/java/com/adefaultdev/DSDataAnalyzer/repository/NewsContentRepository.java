package com.adefaultdev.DSDataAnalyzer.repository;

import com.adefaultdev.DSDataAnalyzer.model.NewsContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface NewsContentRepository extends JpaRepository<NewsContent, Long> {

    List<NewsContent> findBySiteId(Long siteId);
    List<NewsContent> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<NewsContent> findByThemeContainingIgnoreCase(String keyword);
    List<NewsContent> findByDateAfter(LocalDateTime fromDate);

}