package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for creating structured prompt for DeepSeek.
 *
 * @since 1.1.0
 * author ADefaultDev
 */
@Service
public class PromptBuilderService {

    /**
     * Create prompt for a list of news for comparative analysis.
     *
     * @param newsList list of news to analyze
     * @return ready-to-use prompt
     */
    public String buildPrompt(List<NewsContentDTO> newsList) {
        String newsBlock = newsList.stream()
                .map(news -> String.format(
                        "{ \"site\": \"%s\", \"theme\": \"%s\", \"content\": \"%s\" }",
                        news.siteName() != null ? news.siteName() : "unknown site",
                        news.theme() != null ? news.theme() : "no theme",
                        news.content() != null ? news.content() : "no text"
                ))
                .collect(Collectors.joining(",\n"));

        return "You receive a list of news articles in JSON format. "
                + "Compare them with each other and evaluate the credibility of each one on a scale from 1 to 5:\n"
                + "1 - almost certainly false\n"
                + "2 - likely false\n"
                + "3 - cannot be determined (insufficient or contradictory information)\n"
                + "4 - likely true\n"
                + "5 - almost certainly true\n\n"
                + "The rating must depend on how consistent each article is with the others. "
                + "If most articles agree and one contradicts, the contradictory one should get a low rating. "
                + "If it’s impossible to decide, assign 3.\n\n"
                + "Return the result strictly as a JSON array, with each object containing:\n"
                + "{\n"
                + "  \"site\": \"...\",\n"
                + "  \"theme\": \"...\",\n"
                + "  \"summary\": \"short summary (1–2 sentences)\",\n"
                + "  \"rating\": number from 1 to 5\n"
                + "}\n\n"
                + "Here is the input list:\n[\n" + newsBlock + "\n]";
    }
}
