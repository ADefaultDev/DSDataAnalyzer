package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.service.GoogleNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to fetch the news from Google RSS.
 * Provides API to request the news fetching.
 *
 * <p>Dependencies injected via Lombok's @RequiredArgsConstructor:</p>
 * <ul>
 *   <li>{@link GoogleNewsService} - Handles fetched news parsing and saving</li>
 * </ul>
 * @since 1.0.0
 * @author ADefaultDev
 */
@RestController
@RequestMapping("/api/google-news")
@RequiredArgsConstructor
public class GoogleNewsController {

    private final GoogleNewsService googleNewsService;

    /**
     * Handles news fetching from Google RSS
     * Both admins and users can use this endpoint
     *
     * @return ResponseEntity with success or error message
     */
    @Operation(summary = "Fetching the news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News fetched successfully")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchNews(
            @Parameter(description = "Keyword for Google News search", example = "USA")
            @RequestParam String keyword,

            @Parameter(description = "Max number of news to fetch", example = "10")
            @RequestParam int limit
    ) throws Exception {
        googleNewsService.fetchAndSaveNews(keyword, limit);
        return ResponseEntity.ok("News fetched and saved successfully");
    }
}
