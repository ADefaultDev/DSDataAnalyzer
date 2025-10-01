package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.entity.NewsContent;
import com.adefaultdev.DSDataAnalyzer.entity.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsContentRepository;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link NewsContentService}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
@ExtendWith(MockitoExtension.class)
class NewsContentServiceTest {

    @Mock
    private NewsContentRepository newsContentRepository;

    @Mock
    private NewsSiteRepository newsSiteRepository;

    @InjectMocks
    private NewsContentService newsContentService;

    @Test
    void getFreshNews_shouldReturnNewsList() {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        NewsContent news = new NewsContent();
        when(newsContentRepository.findByDateAfter(fromDate)).thenReturn(List.of(news));

        List<NewsContent> result = newsContentService.getFreshNews(fromDate);

        assertThat(result).hasSize(1).contains(news);
        verify(newsContentRepository).findByDateAfter(fromDate);
    }

    @Test
    void getContentById_shouldReturnDTO_whenNewsExists() {
        NewsContent news = new NewsContent();
        news.setId(1L);
        news.setTheme("Tech");
        news.setContent("AI news");
        NewsSite site = new NewsSite();
        site.setId(10L);
        site.setName("Example");
        news.setSite(site);

        when(newsContentRepository.findById(1L)).thenReturn(Optional.of(news));

        NewsContentDTO dto = newsContentService.getContentById(1L);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.theme()).isEqualTo("Tech");
        assertThat(dto.siteId()).isEqualTo(10L);
        verify(newsContentRepository).findById(1L);
    }

    @Test
    void getContentById_shouldThrow_whenNewsNotFound() {
        when(newsContentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> newsContentService.getContentById(99L));
    }

    @Test
    void createContent_shouldSaveAndReturnDTO() {
        NewsSite site = new NewsSite();
        site.setId(10L);
        site.setName("Example");

        NewsContentDTO dto = new NewsContentDTO(
                null, "Tech", "AI news", LocalDateTime.now(), 10L, "Example");

        when(newsSiteRepository.findById(10L)).thenReturn(Optional.of(site));
        when(newsContentRepository.save(any(NewsContent.class))).thenAnswer(inv -> {
            NewsContent n = inv.getArgument(0);
            n.setId(1L);
            return n;
        });

        NewsContentDTO result = newsContentService.createContent(dto);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.theme()).isEqualTo("Tech");
        verify(newsSiteRepository).findById(10L);
        verify(newsContentRepository).save(any(NewsContent.class));
    }
}
