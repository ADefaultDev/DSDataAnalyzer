package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.service.NewsContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller providing news content management.
 * Provides API to request creating or getting news.
 *
 * <p>Dependencies injected via Lombok's @RequiredArgsConstructor:</p>
 * <ul>
 *   <li>{@link NewsContentService} - Handles news content saving and retrieving</li>
 * </ul>
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsContentController {

    private final NewsContentService newsContentService;

    /**
     * Provides news by id
     * Available for admins only
     *
     * @param id unique id of site needed
     * @return NewsContentDTO with news from database
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public NewsContentDTO getNewsById(@PathVariable Long id) {
        return newsContentService.getContentById(id);
    }

    /**
     * Creates news in database
     * Available for admins only
     *
     * @param dto of the site creating
     * @return NewsContentDTO with news created
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public NewsContentDTO createNews(@RequestBody NewsContentDTO dto) {
        return newsContentService.createContent(dto);
    }
}
