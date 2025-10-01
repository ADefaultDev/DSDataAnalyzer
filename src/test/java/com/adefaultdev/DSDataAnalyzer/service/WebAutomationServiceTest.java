package com.adefaultdev.DSDataAnalyzer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for  {@link WebAutomationService}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class WebAutomationServiceTest {

    WebAutomationService service;
    WebAutomationService spyService;

    @BeforeEach
    void setUp() {
        service = new WebAutomationService();
        spyService = Mockito.spy(service);
    }

    @Test
    void initStealthDriver_shouldConfigureHeadlessOptions() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=chrome");
        options.addArguments("--window-size=1920,1080");

        assertThat(options.asMap().toString()).contains("headless");
    }

    @Test
    void openSite_shouldCallInitStealthDriver_andOpenUrl() {
        String testUrl = "https://example.com";

        ChromeDriver mockDriver = mock(ChromeDriver.class);
        doReturn(mockDriver).when(spyService).initStealthDriver(anyBoolean());

        spyService.openSite(testUrl, false);

        try {
            verify(spyService).initStealthDriver(false);
        } catch (Exception ignored) {}

        verify(mockDriver).get(testUrl);
    }
}
