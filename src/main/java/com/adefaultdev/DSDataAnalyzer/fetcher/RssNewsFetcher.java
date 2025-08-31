package com.adefaultdev.DSDataAnalyzer.fetcher;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URI;
import java.net.URLConnection;
import java.util.List;

/**
 * Fetches and processes RSS feed entries from a specified URL.
 * This class provides functionality to retrieve RSS feed data, handle connection timeouts,
 * and preprocess feed entries for consistent link handling.
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>RSS feed retrieval with configurable timeout settings</li>
 *   <li>Automatic link normalization using source URI when available</li>
 *   <li>Thread-safe implementation with immutable configuration</li>
 * </ul>
 *
 * @author ADefaultDev
 * @since 1.0
 */
public class RssNewsFetcher {

    private final String feedUrl;

    /**
     * Constructs a new RssNewsFetcher with the specified feed URL.
     *
     * @param feedUrl the URL of the RSS feed to fetch (must not be null or empty)
     * @throws IllegalArgumentException if feedUrl is null or empty
     */
    public RssNewsFetcher(String feedUrl) {
        if (feedUrl == null || feedUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Feed URL cannot be null or empty");
        }
        this.feedUrl = feedUrl;
    }

    /**
     * Fetches raw RSS feed entries from the configured URL.
     * Establishes a connection with timeout settings, parses the RSS feed,
     * and processes entries to ensure proper link handling.
     *
     * <p>Processing Details:</p>
     * <ul>
     *   <li>Sets connection timeout to 10 seconds</li>
     *   <li>Sets read timeout to 10 seconds</li>
     *   <li>Normalizes entry links by using source URI when available</li>
     * </ul>
     *
     * @return a list of {@link SyndEntry} objects representing the RSS feed entries
     * @throws Exception if any error occurs during feed retrieval or parsing
     */
    public List<SyndEntry> fetchRawEntries() throws Exception {
        URI feedSource = new URI(feedUrl);
        URLConnection connection = feedSource.toURL().openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        try (XmlReader reader = new XmlReader(connection.getInputStream())) {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(reader);

            for (SyndEntry entry : feed.getEntries()) {
                if (entry.getSource() != null && entry.getSource().getUri() != null) {
                    entry.setLink(entry.getSource().getUri());
                }
            }

            return feed.getEntries();
        }
    }
}
