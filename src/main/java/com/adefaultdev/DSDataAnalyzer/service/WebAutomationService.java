package com.adefaultdev.DSDataAnalyzer.service;

import com.codeborne.selenide.WebDriverRunner;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v139.network.Network;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsible for automating browser actions with ChromeDriver.
 * This service is focused on:
 * <ul>
 *   <li>Initializing ChromeDriver with "stealth" options to make Cloudflare passable by manual captcha solving.</li>
 *   <li>Opening a target website with custom browser settings such as user-agent, disabled automation flags, etc.</li>
 *   <li>Applying JavaScript overrides to mask the automation environment.</li>
 * </ul>
 *
 * @since 1.1.0
 * @author ADefaultDev
 */
@Service
@RequiredArgsConstructor
public class WebAutomationService {

    /**
     * Opens a given URL using a ChromeDriver instance initialized in "stealth mode".
     *
     * @param url      the website URL to open
     * @param headless whether to run the browser in headless mode (without GUI)
     */
    public void openSite(String url, boolean headless) {
        ChromeDriver driver;
        try {
            driver = initStealthDriver(headless);
            WebDriverRunner.setWebDriver(driver);

            driver.get(url);

        } catch (Exception ignored) {

        }
    }

    /**
     * Initializes a ChromeDriver instance with stealth options to avoid bot detection.
     * <p>
     * This includes:
     * <ul>
     *   <li>Custom User-Agent string</li>
     *   <li>Disabling Chrome automation flags</li>
     *   <li>Overriding WebDriver-specific properties in JavaScript</li>
     *   <li>Configuring DevTools to override network-level User-Agent</li>
     *   <li>Disabling certain Chrome features (extensions, background networking, GPU, etc.)</li>
     * </ul>
     *
     * @param headless whether to run the browser in headless mode
     * @return a configured ChromeDriver instance
     */
    private ChromeDriver initStealthDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=chrome");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) "
                + "Chrome/128.0.0.0 Safari/537.36";
        options.addArguments("--user-agent=" + userAgent);

        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--lang=en-US,en");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.geolocation", 1);
        options.setExperimentalOption("prefs", prefs);

        options.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        ChromeDriver driver = new ChromeDriver(options);

        try {
            WebDriverRunner.setWebDriver(driver);

            try {
                DevTools devTools = driver.getDevTools();
                devTools.createSession();
                devTools.send(Network.enable(Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));
                devTools.send(Network.setUserAgentOverride(userAgent,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));

            } catch (Exception ignored) {
            }

            // Apply JavaScript overrides to mask automation environment
            ((JavascriptExecutor) driver).executeScript(
                    "Object.defineProperty(navigator, 'webdriver', {get: () => undefined});"
            );

            ((JavascriptExecutor) driver).executeScript(
                    "window.chrome = window.chrome || {runtime: {}};"
            );

            ((JavascriptExecutor) driver).executeScript(
                    "Object.defineProperty(navigator, 'plugins', {get: () => [1,2,3,4]});" +
                            "Object.defineProperty(navigator, 'languages', {get: () => ['en-US','en']});"
            );

            ((JavascriptExecutor) driver).executeScript(
                    "const originalQuery = window.navigator.permissions.query;" +
                            "window.navigator.permissions.query = (parameters) => (" +
                            "  parameters.name === 'notifications' ? Promise.resolve({ state: Notification.permission }) : originalQuery(parameters)" +
                            ");"
            );

            ((JavascriptExecutor) driver).executeScript(
                    "if (navigator.plugins && navigator.plugins.length) {" +
                            "  /* noop - keep plugins array */" +
                            "}"
            );

        } catch (Exception e) {
            try {
                driver.quit();
            } catch (Exception ignore) {}
            throw (RuntimeException)e;
        }

        return driver;
    }

}
