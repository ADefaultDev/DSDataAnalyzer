package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.AiPromptResponse;
import com.adefaultdev.DSDataAnalyzer.service.DeepSeekWebService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

/**
 * REST controller for interacting with the DeepSeek AI service.
 * <p>
 * Exposes endpoints under {@code /api/ai} that allow clients
 * to send prompts to DeepSeek and receive AI-generated responses.
 * </p>
 *
 * @since 1.1.0
 * @author ADefaultDev
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class DeepSeekController {

    private final DeepSeekWebService deepSeekWebService;

    /**
     * Sends a predefined prompt (built from local news content or configuration)
     * to the DeepSeek AI service and returns the response.
     * Accessible only to users with roles ADMIN or USER.
     *
     * @return {@link ResponseEntity} containing {@link AiPromptResponse}
     * - On success: AI response text, timestamp, success flag set to {@code true}
     * - On failure: empty response, timestamp, success flag set to {@code false}
     */
    @Operation(
            summary = "Send fixed prompt from file/application.yml to AI",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Prompt processed successfully"),
                    @ApiResponse(responseCode = "500", description = "AI service error")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/request")
    public ResponseEntity<AiPromptResponse> sendPromptToAi() {
        try {
            String result = deepSeekWebService.sendPrompt();

            AiPromptResponse response = AiPromptResponse.builder()
                    .response(result)
                    .timestamp(LocalDateTime.now())
                    .success(true)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            AiPromptResponse errorResponse = AiPromptResponse.builder()
                    .response(null)
                    .timestamp(LocalDateTime.now())
                    .success(false)
                    .build();

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
