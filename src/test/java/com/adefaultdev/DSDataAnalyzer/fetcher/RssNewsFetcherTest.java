package com.adefaultdev.DSDataAnalyzer.fetcher;

import com.adefaultdev.DSDataAnalyzer.config.AdminInitializer;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link RssNewsFetcher}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class RssNewsFetcherTest {

    @BeforeEach
    void setUp() throws Exception {
        RssNewsFetcher fetcher = new RssNewsFetcher("http://example.com/rss");

        SyndFeedInput mockFeedInput = mock(SyndFeedInput.class);
        fetcher.feedInput = mockFeedInput;

        SyndFeed mockFeed = mock(SyndFeed.class);
        SyndEntry mockEntry = mock(SyndEntry.class);

        when(mockFeed.getEntries()).thenReturn(List.of(mockEntry));
        when(mockFeedInput.build(any(XmlReader.class))).thenReturn(mockFeed);
    }

    @Test
    void fetchRawEntries_shouldReturnProcessedEntries() throws Exception {
        RssNewsFetcher fetcher = spy(new RssNewsFetcher("http://example.com/rss"));

        SyndFeedInput mockFeedInput = mock(SyndFeedInput.class);
        SyndFeed mockFeed = mock(SyndFeed.class);
        SyndEntry mockEntry = mock(SyndEntry.class);

        when(mockFeed.getEntries()).thenReturn(List.of(mockEntry));
        when(mockFeedInput.build(any(XmlReader.class))).thenReturn(mockFeed);

        fetcher.feedInput = mockFeedInput;

        doReturn(mock(XmlReader.class)).when(fetcher).createXmlReader();

        List<SyndEntry> entries = fetcher.fetchRawEntries();

        assertNotNull(entries);
        assertEquals(1, entries.size());
        verify(mockFeedInput).build(any(XmlReader.class));
    }

    @Test
    void constructor_shouldThrowException_whenUrlIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new RssNewsFetcher(null));
    }

    @Test
    void constructor_shouldThrowException_whenUrlIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new RssNewsFetcher(" "));
    }
}
