package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.entity.NewsContent;
import com.adefaultdev.DSDataAnalyzer.entity.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsContentRepository;
import com.adefaultdev.DSDataAnalyzer.security.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DeepSeekWebService}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
@ExtendWith(MockitoExtension.class)
class DeepSeekWebServiceTest {

    @Mock
    private WebAutomationService webAutomationService;

    @Mock
    private NewsContentRepository newsContentRepository;

    @Mock
    private PromptBuilderService promptBuilderService;

    @InjectMocks
    private DeepSeekWebService deepSeekWebService;

    private NewsContent newsContent;

    @BeforeEach
    void setUp() {
        NewsSite site = new NewsSite();
        site.setId(1L);
        site.setName("TestSite");

        newsContent = new NewsContent();
        newsContent.setId(1L);
        newsContent.setTheme("TestTheme");
        newsContent.setContent("TestContent");
        newsContent.setDate(LocalDateTime.now());
        newsContent.setSite(site);
    }

    @Test
    void sendPrompt_shouldFetchNewsAndBuildPrompt() {
        when(newsContentRepository.findLatestNews(any(Pageable.class)))
                .thenReturn(List.of(newsContent));
        when(promptBuilderService.buildPrompt(any()))
                .thenReturn("Generated prompt");

        assertThrows(Exception.class, () -> deepSeekWebService.sendPrompt());

        verify(newsContentRepository, times(1)).findLatestNews(any(Pageable.class));
        verify(promptBuilderService, times(1)).buildPrompt(any());
        verify(webAutomationService, times(1))
                .openSite(eq("https://chat.deepseek.com/"), eq(false));
    }

    @Test
    void sendPrompt_shouldThrowIfRepositoryReturnsEmpty() {
        when(newsContentRepository.findLatestNews(any(Pageable.class)))
                .thenReturn(List.of());
        when(promptBuilderService.buildPrompt(any()))
                .thenReturn("Generated prompt");

        assertThrows(Exception.class, () -> deepSeekWebService.sendPrompt());

        verify(promptBuilderService, times(1)).buildPrompt(any());
        verify(webAutomationService, times(1))
                .openSite(eq("https://chat.deepseek.com/"), eq(false));
    }
}
