package com.adefaultdev.DSDataAnalyzer.service;

import com.codeborne.selenide.WebDriverRunner;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v139.network.Network;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebAutomationService {

    private final CookieManagerService cookieManagerService;

    @Value("${browser.chrome.userDataDir}")
    private String userDataDir;

    @Value("${browser.chrome.profileDirectory}")
    private String profileDirectory;

    /**
     * Автоматически открывает сайт с подгруженными куками (через настройки из application.yml).
     */
    public void openSite(String url, boolean headless) {

        try {
            ChromeDriver driver = initStealthDriver(headless);
            WebDriverRunner.setWebDriver(driver);

            driver.get(url);

            System.out.println("Сайт открыт с использованием сохранённых cookies.");
        } catch (Exception e) {
            System.out.println("Ошибка при автоматическом открытии сайта: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
                devTools.send(Network.setUserAgentOverride(userAgent, Optional.empty(), Optional.empty(), Optional.empty()));

            } catch (Exception ignored) {
            }

            JavascriptExecutor js = driver;

            js.executeScript(
                    "Object.defineProperty(navigator, 'webdriver', {get: () => undefined});"
            );

            js.executeScript(
                    "window.chrome = window.chrome || {runtime: {}};"
            );

            js.executeScript(
                    "Object.defineProperty(navigator, 'plugins', {get: () => [1,2,3,4]});" +
                            "Object.defineProperty(navigator, 'languages', {get: () => ['en-US','en']});"
            );

            js.executeScript(
                    "const originalQuery = window.navigator.permissions.query;" +
                            "window.navigator.permissions.query = (parameters) => (" +
                            "  parameters.name === 'notifications' ? Promise.resolve({ state: Notification.permission }) : originalQuery(parameters)" +
                            ");"
            );

            js.executeScript(
                    "if (navigator.plugins && navigator.plugins.length) {" +
                            "  /* noop - keep plugins array */" +
                            "}"
            );


        } catch (Exception e) {
            try { driver.quit(); } catch (Exception ignore) {}
            throw (RuntimeException)e;
        }

        return driver;
    }

}
