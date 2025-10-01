package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.entity.NewsContent;
import com.adefaultdev.DSDataAnalyzer.entity.NewsSite;
import com.adefaultdev.DSDataAnalyzer.fetcher.RssNewsFetcher;
import com.adefaultdev.DSDataAnalyzer.repository.NewsContentRepository;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GoogleNewsService}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
@ExtendWith(MockitoExtension.class)
class GoogleNewsServiceTest {

    @Mock
    private NewsSiteRepository newsSiteRepository;

    @Mock
    private NewsContentRepository newsContentRepository;

    @InjectMocks
    private GoogleNewsService googleNewsService;

    @Test
    void fetchAndSaveNews_shouldSaveNewsContentAndSite() throws Exception {
        SyndEntry entry = mock(SyndEntry.class);
        when(entry.getTitle()).thenReturn("Breaking News - CNN");
        when(entry.getLink()).thenReturn("http://cnn.com/article/123");
        when(entry.getPublishedDate()).thenReturn(new Date());

        try (MockedConstruction<RssNewsFetcher> mocked = mockConstruction(
                RssNewsFetcher.class,
                (mock, context) -> when(mock.fetchRawEntries()).thenReturn(List.of(entry))
        )) {
            NewsSite site = new NewsSite();
            site.setId(1L);
            site.setName("CNN");
            site.setAddress("cnn.com");
            site.setProcessedCount(0L);

            when(newsSiteRepository.findByAddress("cnn.com")).thenReturn(Optional.of(site));

            googleNewsService.fetchAndSaveNews("politics", 1);

            verify(newsSiteRepository, times(1)).findByAddress("cnn.com");
            verify(newsContentRepository, times(1)).save(any(NewsContent.class));
            verify(newsSiteRepository, atLeastOnce()).save(any(NewsSite.class));
        }
    }

    @Test
    void fetchAndSaveNews_shouldCreateNewSiteIfNotExists() throws Exception {
        SyndEntry entry = mock(SyndEntry.class);
        when(entry.getTitle()).thenReturn("Some News - Unknown Source");
        when(entry.getLink()).thenReturn("http://unknown.com/article/1");
        when(entry.getPublishedDate()).thenReturn(new Date());

        try (MockedConstruction<RssNewsFetcher> mocked = mockConstruction(
                RssNewsFetcher.class,
                (mock, context) -> when(mock.fetchRawEntries()).thenReturn(List.of(entry))
        )) {
            when(newsSiteRepository.findByAddress("unknown.com")).thenReturn(Optional.empty());
            when(newsSiteRepository.save(any(NewsSite.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            googleNewsService.fetchAndSaveNews("tech", 1);

            verify(newsSiteRepository, atLeastOnce()).save(any(NewsSite.class));
            verify(newsContentRepository).save(any(NewsContent.class));
        }
    }
}
