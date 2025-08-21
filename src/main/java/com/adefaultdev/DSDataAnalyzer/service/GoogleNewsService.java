package com.adefaultdev.DSDataAnalyzer.service;


import com.adefaultdev.DSDataAnalyzer.fetcher.RssNewsFetcher;
import com.adefaultdev.DSDataAnalyzer.model.NewsContent;
import com.adefaultdev.DSDataAnalyzer.model.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsContentRepository;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleNewsService {

    private final NewsSiteRepository newsSiteRepository;
    private final NewsContentRepository newsContentRepository;

    public void fetchAndSaveNews(String keyword, int limit) throws Exception {
        String url = "https://news.google.com/rss/search?q=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        RssNewsFetcher fetcher = new RssNewsFetcher(url);
        List<SyndEntry> entries = fetcher.fetchRawEntries();

        int count = 0;
        for (SyndEntry entry : entries) {
            if (count >= limit) break;

            String sourceName = entry.getSource() != null ? entry.getSource().getTitle() : "Unknown";
            String sourceAddress = entry.getLink();

            NewsSite site = newsSiteRepository.findByName(sourceName)
                    .orElseGet(() -> {
                        NewsSite newSite = new NewsSite();
                        newSite.setName(sourceName);
                        newSite.setAddress(sourceAddress);
                        newSite.setTrustIndex(0.5);
                        return newsSiteRepository.save(newSite);
                    });

            NewsContent news = new NewsContent();
            news.setSite(site);
            news.setTheme(entry.getTitle());
            news.setContent(entry.getDescription() != null ? entry.getDescription().getValue() : "");
            news.setDate(entry.getPublishedDate() != null
                    ? LocalDateTime.ofInstant(entry.getPublishedDate().toInstant(), ZoneId.systemDefault())
                    : LocalDateTime.now());

            newsContentRepository.save(news);

            count++;
        }
    }
}
