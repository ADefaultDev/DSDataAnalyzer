package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.service.NewsContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link NewsContentController}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class NewsContentControllerTest {

    private NewsContentService newsContentService;
    private NewsContentController newsContentController;

    @BeforeEach
    void setUp() {
        newsContentService = mock(NewsContentService.class);
        newsContentController = new NewsContentController(newsContentService);
    }

    /**
     * Tests {@link NewsContentController#getNewsById(Long)} with valid id.
     * Ensures that the service returns expected DTO and is called once.
     */
    @Test
    void testGetNewsById() {
        Long id = 1L;
        NewsContentDTO dto = new NewsContentDTO(
                1L,
                "TestTheme",
                "TestContent",
                LocalDateTime.of(2025,1,1,1,1),
                1L,
                "TestName"
        );
        when(newsContentService.getContentById(id)).thenReturn(dto);

        NewsContentDTO result = newsContentController.getNewsById(id);

        assertNotNull(result);
        assertEquals("TestTheme", result.theme());
        verify(newsContentService, times(1)).getContentById(id);
    }

    /**
     * Tests {@link NewsContentController#createNews(NewsContentDTO)} with valid DTO.
     * Ensures that the service saves the content and returns it back.
     */
    @Test
    void testCreateNews() {
        NewsContentDTO request = new NewsContentDTO(
                1L,
                "TestTheme",
                "TestContent",
                LocalDateTime.of(2025,1,1,1,1),
                1L,
                "TestName"
        );

        NewsContentDTO saved = new NewsContentDTO(
                1L,
                "TestTheme",
                "TestContent",
                LocalDateTime.of(2025,1,1,1,1),
                1L,
                "TestName"
        );

        when(newsContentService.createContent(request)).thenReturn(saved);

        NewsContentDTO result = newsContentController.createNews(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("TestName", result.siteName());
        verify(newsContentService, times(1)).createContent(request);
    }
}
