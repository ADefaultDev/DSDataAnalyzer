package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.service.GoogleNewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link GoogleNewsController}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class GoogleNewsControllerTest {

    private GoogleNewsService googleNewsService;
    private GoogleNewsController googleNewsController;

    @BeforeEach
    void setUp() {
        googleNewsService = mock(GoogleNewsService.class);
        googleNewsController = new GoogleNewsController(googleNewsService);
    }

    /**
     * Tests {@link GoogleNewsController#fetchNews(String, int)} with valid input.
     * Ensures that the service is called with correct arguments and response is successful.
     */
    @Test
    void testFetchNews_Success() throws Exception {
        String keyword = "USA";
        int limit = 5;

        ResponseEntity<String> response = googleNewsController.fetchNews(keyword, limit);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("News fetched and saved successfully", response.getBody());
        verify(googleNewsService, times(1)).fetchAndSaveNews(keyword, limit);
    }

    /**
     * Tests {@link GoogleNewsController#fetchNews(String, int)} when the service throws an exception.
     * Ensures that the exception is propagated to the caller.
     */
    @Test
    void testFetchNews_ServiceThrowsException() throws Exception {
        String keyword = "Tech";
        int limit = 3;
        doThrow(new RuntimeException("Service failure"))
                .when(googleNewsService).fetchAndSaveNews(keyword, limit);

        Exception ex = assertThrows(RuntimeException.class,
                () -> googleNewsController.fetchNews(keyword, limit));

        assertEquals("Service failure", ex.getMessage());
        verify(googleNewsService, times(1)).fetchAndSaveNews(keyword, limit);
    }
}
