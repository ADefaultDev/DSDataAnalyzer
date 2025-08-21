package com.adefaultdev.DSDataAnalyzer.fetcher;

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
import java.util.List;

public class RssNewsFetcher {

    private static final Logger log = LoggerFactory.getLogger(RssNewsFetcher.class);

    private final String feedUrl;

    public RssNewsFetcher(String feedUrl) {
        if (feedUrl == null || feedUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Feed URL cannot be null or empty");
        }
        this.feedUrl = feedUrl;
    }

    public List<SyndEntry> fetchRawEntries() throws IOException, FeedException, URISyntaxException {
        URI feedSource = new URI(feedUrl);
        URLConnection connection = feedSource.toURL().openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        try (XmlReader reader = new XmlReader(connection.getInputStream())) {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(reader);
            return feed.getEntries();
        }
    }
}
