package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.entity.NewsContent;
import com.adefaultdev.DSDataAnalyzer.repository.NewsContentRepository;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing news content operations.
 * Handles retrieval, creation, and conversion of news content.
 *
 * <p>Dependencies injected via Lombok:</p>
 * <ul>
 *   <li>{@link NewsContentRepository} - Handles news content data access</li>
 *   <li>{@link NewsSiteRepository} - Handles news site data access</li>
 * </ul>
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
@Service
@RequiredArgsConstructor
public class NewsContentService {

    private final NewsContentRepository newsContentRepository;
    private final NewsSiteRepository newsSiteRepository;

    /**
     * Retrieves fresh news content after specified date.
     *
     * @param fromDate starting date for news retrieval
     * @return list of news content entities
     */
    public List<NewsContent> getFreshNews(LocalDateTime fromDate) {
        return newsContentRepository.findByDateAfter(fromDate);
    }

    /**
     * Retrieves news content by ID and converts to DTO.
     *
     * @param id news content ID
     * @return NewsContentDTO with content details
     */
    @Transactional(readOnly = true)
    public NewsContentDTO getContentById(Long id) {
        NewsContent content = newsContentRepository.findById(id).orElseThrow();

        return convertToDTO(content);
    }

    /**
     * Creates new news content from DTO.
     *
     * @param dto NewsContentDTO with content details
     * @return created NewsContentDTO
     */
    @Transactional
    public NewsContentDTO createContent(NewsContentDTO dto) {
        NewsContent content = convertToEntity(dto);
        NewsContent saved = newsContentRepository.save(content);

        return convertToDTO(saved);
    }

    /**
     * Converts NewsContent entity to DTO.
     *
     * @param content NewsContent entity
     * @return NewsContentDTO
     */
    private NewsContentDTO convertToDTO(NewsContent content) {
        return new NewsContentDTO(
                content.getId(),
                content.getTheme(),
                content.getContent(),
                LocalDateTime.now(),
                content.getSite().getId(),
                content.getSite().toString());
    }

    /**
     * Converts NewsContentDTO to entity.
     *
     * @param dto NewsContentDTO
     * @return NewsContent entity
     */
    private NewsContent convertToEntity(NewsContentDTO dto) {
        NewsContent content = new NewsContent();
        content.setTheme(dto.theme());
        content.setContent(dto.content());
        content.setDate(dto.date());
        content.setSite(newsSiteRepository.findById(dto.siteId()).orElseThrow());

        return content;
    }

}