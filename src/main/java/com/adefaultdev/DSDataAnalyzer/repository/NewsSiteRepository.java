package com.adefaultdev.DSDataAnalyzer.repository;

import com.adefaultdev.DSDataAnalyzer.model.NewsSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsSiteRepository extends JpaRepository<NewsSite, Long> {

    NewsSite findByAddress(String address);
    boolean existsByAddress(String address);

}