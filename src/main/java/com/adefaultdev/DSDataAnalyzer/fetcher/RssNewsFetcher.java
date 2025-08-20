package com.adefaultdev.DSDataAnalyzer.fetcher;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class RssNewsFetcher {

    private static final Logger log = LoggerFactory.getLogger(RssNewsFetcher.class);

    private final String feedUrl;
    private final Long siteId;
    private final String siteName;

    public RssNewsFetcher(String feedUrl, Long siteId, String siteName) {
        if (feedUrl == null || feedUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Feed URL cannot be null or empty");
        }
        this.feedUrl = feedUrl;
        this.siteId = siteId;
        this.siteName = siteName;
    }

    public List<NewsContentDTO> fetchNews() throws IOException, FeedException, URISyntaxException {
        List<NewsContentDTO> newsList = new ArrayList<>();

        URI feedSource = new URI(feedUrl);
        URLConnection connection = feedSource.toURL().openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        try (XmlReader reader = new XmlReader(connection.getInputStream())) {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(reader);

            for (SyndEntry entry : feed.getEntries()) {
                LocalDateTime publishedDate = null;
                if (entry.getPublishedDate() != null) {
                    publishedDate = LocalDateTime.ofInstant(
                            entry.getPublishedDate().toInstant(),
                            ZoneId.systemDefault()
                    );
                }

                NewsContentDTO dto = new NewsContentDTO(
                        null,
                        entry.getTitle(),
                        entry.getDescription() != null ? entry.getDescription().getValue() : "",
                        publishedDate,
                        siteId,
                        siteName
                );
                newsList.add(dto);
            }
        } catch (IOException | FeedException e) {
            log.error("Failed to fetch RSS from {}", feedUrl, e);
            throw e;
        }

        return newsList;
    }
}
