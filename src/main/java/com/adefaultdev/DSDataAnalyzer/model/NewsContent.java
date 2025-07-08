package com.adefaultdev.DSDataAnalyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "news_contents")
@Data
public class NewsContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private NewsSite site;

    @Column(nullable = false)
    private String theme;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

}