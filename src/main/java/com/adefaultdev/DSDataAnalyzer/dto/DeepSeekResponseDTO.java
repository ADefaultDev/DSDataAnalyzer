package com.adefaultdev.DSDataAnalyzer.dto;

import lombok.Data;

/**
 * Data Transfer Object for the content of DeepSeek response.
 * Contains name, address, trust index and processed count.
 *
 * @author ADefaultDev
 * @since 1.3.0
 */
@Data
public class DeepSeekResponseDTO {
    private String name;
    private String address;
    private Double trustIndex;
    private Long processedCount;
}
