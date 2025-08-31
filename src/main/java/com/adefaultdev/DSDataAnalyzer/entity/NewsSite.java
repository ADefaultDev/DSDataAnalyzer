package com.adefaultdev.DSDataAnalyzer.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import java.util.List;

/**
 * Entity for news sites.
 * Contains all fields for news site class in database.
 *
 * @author ADefaultDev
 * @since 1.0.0
 */
@Entity
@Table(name = "news_sites")
@Data
public class NewsSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false)
    private String name;

    @Column(name = "trust_index")
    private Double trustIndex;

    @Column(name = "processed_count", nullable = false)
    private Long processedCount = 0L;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsContent> contents;
}