package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.model.NewsContent;
import com.adefaultdev.DSDataAnalyzer.repository.NewsContentRepository;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsContentService {

    private final NewsContentRepository newsContentRepository;
    private final NewsSiteRepository newsSiteRepository;

    public List<NewsContent> getFreshNews(LocalDateTime fromDate) {
        return newsContentRepository.findByDateAfter(fromDate);
    }

    @Transactional(readOnly = true)
    public NewsContentDTO getContentById(Long id) {

        NewsContent content = newsContentRepository.findById(id).orElseThrow();
        return convertToDTO(content);

    }

    @Transactional
    public NewsContentDTO createContent(NewsContentDTO dto) {

        NewsContent content = convertToEntity(dto);
        NewsContent saved = newsContentRepository.save(content);
        return convertToDTO(saved);

    }

    private NewsContentDTO convertToDTO(NewsContent content) {

        NewsContentDTO dto = new NewsContentDTO();
        dto.setId(content.getId());
        dto.setTheme(content.getTheme());
        dto.setContent(content.getContent());
        dto.setSiteId(content.getSite().getId());
        return dto;

    }

    private NewsContent convertToEntity(NewsContentDTO dto) {

        NewsContent content = new NewsContent();
        content.setTheme(dto.getTheme());
        content.setContent(dto.getContent());
        content.setSite(newsSiteRepository.findById(dto.getSiteId()).orElseThrow());
        return content;

    }

}