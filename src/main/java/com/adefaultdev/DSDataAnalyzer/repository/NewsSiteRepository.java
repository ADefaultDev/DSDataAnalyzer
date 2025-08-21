package com.adefaultdev.DSDataAnalyzer.repository;

import com.adefaultdev.DSDataAnalyzer.model.NewsSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsSiteRepository extends JpaRepository<NewsSite, Long> {

    NewsSite findByAddress(String address);
    boolean existsByAddress(String address);

    Optional<NewsSite> findByName(String sourceName);
}