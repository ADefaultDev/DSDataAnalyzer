package com.adefaultdev.DSDataAnalyzer.dto;

/**
 * Data Transfer Object for news sites.
 * Contains all necessary data for site.
 *
 * @author ADefaultDev
 * @since 1.0.0
 */
public record NewsSiteDTO(
        Long id,
        String address,
        String name,
        Double trustIndex
) {}
