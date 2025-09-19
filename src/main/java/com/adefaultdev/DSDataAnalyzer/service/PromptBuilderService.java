package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for creating prompt for DeepSeek.
 *
 * @since 1.1.0
 * @author ADefaultDev
 */
@Service
public class PromptBuilderService {

    @Value("${ai.prompt}")
    private String promptTemplate;

    /**
     * Creating prompt for one news.
     *
     * @return ready to use prompt.
     */
    public String buildPrompt(NewsContentDTO news) {
        return promptTemplate
                .replace("{siteName}", news.siteName() != null ? news.siteName() : "unknown site")
                .replace("{theme}", news.theme() != null ? news.theme() : "no theme")
                .replace("{content}", news.content() != null ? news.content() : "no text");
    }

    /**
     * Creating prompt for list of news.
     *
     * @return ready to use prompt.
     */
    public String buildPrompt(List<NewsContentDTO> newsList) {
        String newsBlock = newsList.stream()
                .map(this::buildPrompt)
                .collect(Collectors.joining("\n\n"));

        return "Analyze the news and return the result:\n\n" + newsBlock;
    }
}
