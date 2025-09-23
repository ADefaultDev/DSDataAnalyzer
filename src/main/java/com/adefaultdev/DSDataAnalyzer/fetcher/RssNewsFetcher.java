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

    SyndFeedInput feedInput = new SyndFeedInput();

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
     * Creates an {@link XmlReader} from the feed URL.
     * <p>
     * Opens a connection to the configured RSS feed URL with a 10-second
     * connect and read timeout.
     * </p>
     *
     * @return {@link XmlReader} for reading the RSS feed
     * @throws Exception if the URL is malformed or connection cannot be established
     */
    protected XmlReader createXmlReader() throws Exception {
        URI feedSource = new URI(feedUrl);
        URLConnection connection = feedSource.toURL().openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        return new XmlReader(connection.getInputStream());
    }


    /**
     * Fetches raw RSS feed entries from the configured feed URL.
     * <p>
     * Establishes a connection using {@link #createXmlReader()}, parses the RSS feed,
     * and normalizes each entry's link using {@link SyndEntry#getSource() source URI} if present.
     * </p>
     *
     * <p>Processing details:</p>
     * <ul>
     *     <li>Connection timeout: 10 seconds</li>
     *     <li>Read timeout: 10 seconds</li>
     *     <li>Links are normalized using source URI if available</li>
     * </ul>
     *
     * @return list of {@link SyndEntry} objects representing the RSS feed entries
     * @throws Exception if any error occurs during feed retrieval or parsing
     */
    public List<SyndEntry> fetchRawEntries() throws Exception {
        try (XmlReader reader = createXmlReader()) {
            SyndFeed feed = feedInput.build(reader);
            for (SyndEntry entry : feed.getEntries()) {
                if (entry.getSource() != null && entry.getSource().getUri() != null) {
                    entry.setLink(entry.getSource().getUri());
                }
            }
            return feed.getEntries();
        }
    }
}
