package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.repository.NewsContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;

/**
 * Service for managing DeepSeek via browser.
 * Handles prompt sending and answer retrieving.
 *
 * <p>Dependencies injected via Lombok:</p>
 * <ul>
 *   <li>{@link WebAutomationService} - Manages browser setup </li>
 *   <li>{@link NewsContentRepository} - Manages news content data</li>
 *   <li>{@link PromptBuilderService} - Manages prompt creation</li>
 * </ul>
 *
 * @since 1.1.0
 * @author ADefaultDev
 */
@Service
@RequiredArgsConstructor
public class DeepSeekWebService {

    private final WebAutomationService webAutomationService;
    private final NewsContentRepository newsContentRepository;
    private final PromptBuilderService promptBuilderService;

    @Value("${deepseek.email}")
    private String email;

    @Value("${deepseek.password}")
    private String password;

    /**
     * Sending prompt to DeepSeek chat.
     * Uses webAutomationService to open site, then inserts email and password.
     * After authentication sends prompt to chat.
     *
     * @throws InterruptedException if this method's thread is interrupted
     */
    public String sendPrompt() throws InterruptedException {
        List<NewsContentDTO> latestNews = newsContentRepository
                .findLatestNews(PageRequest.of(0, 10))
                .stream()
                .map(news -> new NewsContentDTO(
                        news.getId(),
                        news.getTheme(),
                        news.getContent(),
                        news.getDate(),
                        news.getSite().getId(),
                        news.getSite().getName()
                ))
                .toList();

        String prompt = promptBuilderService.buildPrompt(latestNews);

        webAutomationService.openSite("https://chat.deepseek.com/", false);

        Thread.sleep(30000);

        $("input[placeholder='Phone number / email address']").should(appear).setValue(email);

        $("input[placeholder='Password']").setValue(password);

        $("div.ds-sign-up-form__register-button[role='button']")
                .should(appear)
                .scrollIntoView(true)
                .click();

        $("textarea").should(appear).setValue(prompt).pressEnter();

        return $(".response-container").should(appear).getText();
    }

}
