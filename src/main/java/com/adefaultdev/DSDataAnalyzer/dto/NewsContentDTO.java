package com.adefaultdev.DSDataAnalyzer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NewsContentDTO {

    private Long id;
    private String theme;
    private String content;
    private LocalDateTime date;
    private Long siteId;
    private String siteName;

}