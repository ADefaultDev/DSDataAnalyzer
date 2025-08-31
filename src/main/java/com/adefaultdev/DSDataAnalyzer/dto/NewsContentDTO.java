package com.adefaultdev.DSDataAnalyzer.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for user news content.
 * Contains all pivotal data for news content.
 *
 * @author ADefaultDev
 * @since 1.0.0
 */
public record NewsContentDTO(
        Long id,
        String theme,
        String content,
        LocalDateTime date,
        Long siteId,
        String siteName
) {}