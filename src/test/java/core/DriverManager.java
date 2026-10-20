package core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.Map;


public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>(); //tambahkan ini

    public static void initDriver(String browser) {
        WebDriver webDriver = null;
        switch (browser.toLowerCase()) {
            case "chrome" -> {
                String githubActions = System.getenv("GITHUB_ACTIONS");
                boolean isCI = githubActions != null && githubActions.equals("true");


                System.out.println("=== Driver Initialization ===");
                System.out.println("Running in CI: " + isCI);


                if (!isCI) {
                    System.out.println("Setting up ChromeDriver via WebDriverManager");
                    WebDriverManager.chromedriver().setup();
                } else {
                    System.out.println("Using pre-installed ChromeDriver from CI");
                    String chromeDriverPath = System.getenv("CHROMEDRIVER_PATH");
                    if (chromeDriverPath != null && !chromeDriverPath.isEmpty()) {
                        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                        System.out.println("ChromeDriver path set to: " + chromeDriverPath);
                    }
                }


                ChromeOptions options = new ChromeOptions();


                options.addArguments("--disable-blink-features=AutomationControlled");
                options.addArguments("--disable-web-resources");
                options.addArguments("--disable-extensions");
                options.addArguments("--disable-plugins");
                options.addArguments("--disable-component-extensions-with-background-pages");
                options.setExperimentalOption("useAutomationExtension", false);
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

                // Disable Chrome "Save password?" and "Change your password" (leak detection) popups
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments("--disable-save-password-bubble");
                options.addArguments("--disable-features=PasswordLeakDetection");

                if (isCI) {
                    System.out.println("Configuring Chrome for CI environment");
                    String chromeBin = System.getenv("CHROME_BIN");
                    if (chromeBin != null && !chromeBin.isEmpty()) {
                        options.setBinary(chromeBin);
                        System.out.println("Chrome binary set to: " + chromeBin);
                    } else {
                        System.out.println("CHROME_BIN not set, using default");
                    }


                    options.addArguments("--headless=new");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--window-size=1920,1080");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-setuid-sandbox");
                    options.addArguments("--remote-debugging-port=9222");
                    options.addArguments("--disable-software-rasterizer");
                    options.addArguments("--remote-allow-origins=*");
                    System.out.println("Headless mode enabled with CI-specific options");
                } else {
                    options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                }


                try {
                    System.out.println("Creating ChromeDriver instance...");
                    webDriver = new ChromeDriver(options);
                    System.out.println("ChromeDriver created successfully!");
                } catch (Exception e) {
                    System.err.println("Failed to create ChromeDriver: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver();
            }
        }
        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
