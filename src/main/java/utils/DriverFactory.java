package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.*;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver initDriver() {

        if (driver.get() == null) {

            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // ✅ Headed preferred for MMT (use headless only in CI)
            // options.addArguments("--headless=new");

            options.addArguments("--start-maximized");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-extensions");
            options.setExperimentalOption("useAutomationExtension", false);
            // ✅ Remove automation flags
            options.setExperimentalOption("excludeSwitches",
                    new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--lang=en-In");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-geolocation");
            WebDriver webDriver = new ChromeDriver(options);


            driver.set(webDriver);
        }

        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}