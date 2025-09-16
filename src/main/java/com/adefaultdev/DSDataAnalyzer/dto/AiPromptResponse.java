package com.adefaultdev.DSDataAnalyzer.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for AI response.
 * Contains response itself, timestamp and if AI responded correctly.
 *
 * @author ADefaultDev
 * @since 1.1.0
 */
@Data
@Builder
public class AiPromptResponse {
    private String response;
    private LocalDateTime timestamp;
    private boolean success;
}