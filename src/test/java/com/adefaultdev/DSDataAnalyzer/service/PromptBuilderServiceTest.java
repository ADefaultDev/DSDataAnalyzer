package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for  {@link PromptBuilderService}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class PromptBuilderServiceTest {

    private PromptBuilderService promptBuilderService;

    @BeforeEach
    void setUp() {
        promptBuilderService = new PromptBuilderService();
    }

    @Test
    void buildPrompt_shouldIncludeAllNews() {
        NewsContentDTO news1 = new NewsContentDTO(
                1L, "Politics", "Some political content",
                LocalDateTime.now(), 10L, "SiteA"
        );
        NewsContentDTO news2 = new NewsContentDTO(
                2L, "Economy", "Economic content",
                LocalDateTime.now(), 20L, "SiteB"
        );

        String result = promptBuilderService.buildPrompt(List.of(news1, news2));

        assertThat(result).contains("SiteA");
        assertThat(result).contains("SiteB");
        assertThat(result).contains("Politics");
        assertThat(result).contains("Economy");
    }

    @Test
    void buildPrompt_shouldUseFallbackValuesForNulls() {
        NewsContentDTO news = new NewsContentDTO(
                1L, null, null, LocalDateTime.now(), 10L, null
        );

        String result = promptBuilderService.buildPrompt(List.of(news));

        assertThat(result).contains("unknown site");
        assertThat(result).contains("no theme");
        assertThat(result).contains("no text");
    }

    @Test
    void buildPrompt_shouldContainInstructionBlock() {
        NewsContentDTO news = new NewsContentDTO(
                1L, "Tech", "AI is evolving",
                LocalDateTime.now(), 11L, "TechSite"
        );

        String result = promptBuilderService.buildPrompt(List.of(news));

        assertThat(result).contains("You receive a list of news articles in JSON format");
        assertThat(result).contains("\"rating\": number from 1 to 5");
        assertThat(result).contains("\"site\": \"TechSite\"");
    }
}
