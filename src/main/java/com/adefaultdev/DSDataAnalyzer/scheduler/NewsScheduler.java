package com.adefaultdev.DSDataAnalyzer.scheduler;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.fetcher.RssNewsFetcher;
import com.adefaultdev.DSDataAnalyzer.service.NewsSiteService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsScheduler {

    private final NewsSiteService newsService;

    public NewsScheduler(NewsSiteService newsService) {
        this.newsService = newsService;
    }

    // каждые 10 минут
    @Scheduled(fixedRate = 600_000)
    public void fetchAndSaveNews() throws Exception {
        RssNewsFetcher fetcher = new RssNewsFetcher(
                "https://news.ycombinator.com/rss", 1L, "HackerNews"
        );

        List<NewsContentDTO> news = fetcher.fetchNews("java");
        newsService.saveNews(news);
    }
}
