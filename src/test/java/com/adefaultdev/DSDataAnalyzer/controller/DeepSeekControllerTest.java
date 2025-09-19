package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.AiPromptResponse;
import com.adefaultdev.DSDataAnalyzer.service.DeepSeekWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link DeepSeekController}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class DeepSeekControllerTest {

    private DeepSeekWebService deepSeekWebService;
    private DeepSeekController deepSeekController;

    @BeforeEach
    void setUp() {
        deepSeekWebService = mock(DeepSeekWebService.class);
        deepSeekController = new DeepSeekController(deepSeekWebService);
    }

    /**
     * Tests {@link DeepSeekController#sendPromptToAi()} when the service returns a valid response.
     * Ensures that the response contains the expected AI output and success is true.
     */
    @Test
    void testSendPromptToAi_Success() throws InterruptedException {
        String aiResponse = "AI generated response";
        when(deepSeekWebService.sendPrompt()).thenReturn(aiResponse);

        ResponseEntity<AiPromptResponse> responseEntity = deepSeekController.sendPromptToAi();

        assertEquals(200, responseEntity.getStatusCode().value());
        AiPromptResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(aiResponse, body.getResponse());
        assertTrue(body.isSuccess());
        assertNotNull(body.getTimestamp());
        verify(deepSeekWebService, times(1)).sendPrompt();
    }

    /**
     * Tests {@link DeepSeekController#sendPromptToAi()} when the service throws an exception.
     * Ensures that the response indicates failure with status 500 and success is false.
     */
    @Test
    void testSendPromptToAi_Failure() throws InterruptedException {
        when(deepSeekWebService.sendPrompt()).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<AiPromptResponse> responseEntity = deepSeekController.sendPromptToAi();

        assertEquals(500, responseEntity.getStatusCode().value());
        AiPromptResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertNull(body.getResponse());
        assertFalse(body.isSuccess());
        assertNotNull(body.getTimestamp());
        verify(deepSeekWebService, times(1)).sendPrompt();
    }
}
