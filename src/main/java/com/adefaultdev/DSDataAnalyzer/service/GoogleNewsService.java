package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.fetcher.RssNewsFetcher;
import com.adefaultdev.DSDataAnalyzer.entity.NewsContent;
import com.adefaultdev.DSDataAnalyzer.entity.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsContentRepository;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Service for fetching and processing Google News RSS feeds.
 * Handles news retrieval, source extraction, and database storage.
 *
 * <p>Dependencies injected via Lombok:</p>
 * <ul>
 *   <li>{@link NewsSiteRepository} - Manages news site data</li>
 *   <li>{@link NewsContentRepository} - Manages news content data</li>
 * </ul>
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleNewsService {

    private final NewsSiteRepository newsSiteRepository;
    private final NewsContentRepository newsContentRepository;

    @Value("${google.news.base-url}")
    private String baseUrl;

    /**
     * Fetches news for a keyword and saves to database.
     * Processes RSS feed entries up to specified limit.
     *
     * @param keyword Search keyword for news
     * @param limit Maximum number of entries to save
     * @throws Exception if RSS fetch or processing fails
     */
    public void fetchAndSaveNews(String keyword, int limit) throws Exception {
        String url = baseUrl + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        RssNewsFetcher fetcher = new RssNewsFetcher(url);
        List<SyndEntry> entries = fetcher.fetchRawEntries();

        int saved = 0;
        for (SyndEntry entry : entries) {
            if (saved >= limit) break;

            String originalUrl = entry.getLink();

            String sourceName = extractSourceFromTitle(entry.getTitle());

            log.warn("Link is ='{}'", originalUrl);

            String host = null;
            try {
                host = new java.net.URI(originalUrl).getHost();
            } catch (Exception ignore) {}
            if (host == null) host = "unknown";
            if (host.startsWith("www.")) host = host.substring(4);

            if ("unknown".equals(sourceName)) {
                sourceName = host;
            }

            String finalHost = host;
            String finalSourceName = sourceName;
            NewsSite site = newsSiteRepository.findByAddress(host)
                    .orElseGet(() -> {
                        NewsSite ns = new NewsSite();
                        ns.setName(finalSourceName);
                        ns.setAddress(finalHost);
                        ns.setTrustIndex(0.0);
                        ns.setProcessedCount(0L);
                        return newsSiteRepository.save(ns);
                    });

            NewsContent news = new NewsContent();
            news.setSite(site);
            news.setTheme(keyword);
            news.setContent(entry.getTitle());
            news.setDate(entry.getPublishedDate() != null
                    ? LocalDateTime.ofInstant(entry.getPublishedDate().toInstant(),
                    ZoneId.systemDefault())
                    : LocalDateTime.now());

            newsContentRepository.save(news);

            site.setProcessedCount(site.getProcessedCount() == null ? 1 :
                    site.getProcessedCount() + 1);
            newsSiteRepository.save(site);

            saved++;
        }

        log.info("Saved {} entries for keyword='{}'", saved, keyword);
    }

    /**
     * Extracts source name from news title using hyphen delimiter.
     *
     * @param title News article title
     * @return Extracted source name or "unknown"
     */
    private String extractSourceFromTitle(String title) {
        if (title == null) return "unknown";
        String[] parts = title.split("-");
        if (parts.length > 1) {
            return parts[parts.length - 1].trim();
        }
        return "unknown";
    }
}

