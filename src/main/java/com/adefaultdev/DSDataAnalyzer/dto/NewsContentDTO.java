package com.adefaultdev.DSDataAnalyzer.dto;

import java.time.LocalDateTime;

public record NewsContentDTO(
        Long id,
        String theme,
        String content,
        LocalDateTime date,
        Long siteId,
        String siteName
) {}