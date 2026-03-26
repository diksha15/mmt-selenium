package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtil;

import java.time.Duration;
import java.util.List;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Logger log;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.log = LoggerUtil.getLogger(this.getClass());
    }

    // ===================== WAITS =====================

    protected WebElement waitForVisibility(WebElement element) {
        log.info("Waiting for visibility of element: {}", element);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForInvisibility(WebElement element) {
        log.info("Waiting for element to disappear: {}", element);
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    protected void waitForLoaderToDisappear() {
        try {
            By loader = By.xpath("//div[contains(@class,'loader') or contains(@class,'spinner')]");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
            log.info("Loader disappeared");
        } catch (Exception e) {
            log.info("No loader present");
        }
    }

    // ===================== ACTIONS =====================

    protected void safeClick(WebElement element) {
        try {
            closeOverlays();
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            log.info("Clicked WebElement: {}", element);
        } catch (ElementClickInterceptedException e) {
            log.warn("Click intercepted, using JS click: {}", element);
            jsClick(element);
        } catch (Exception e) {
            log.error("Failed to click WebElement: {}", element, e);
            jsClick(element);
        }
    }

    protected void safeType(WebElement element, String text) {
        try {
            waitForVisibility(element);
            element.clear();
            element.sendKeys(text);
            log.info("Typed '{}' into {}", text, element);
        } catch (Exception e) {
            log.warn("Normal sendKeys failed, using JS set value for {}", element);
            jsType(element, text);
        }
    }

    protected String safeGetText(WebElement element) {
        try {
            return waitForVisibility(element).getText();
        } catch (Exception e) {
            log.warn("Failed to get text for {}", element);
            return "";
        }
    }

    // ===================== JS ACTIONS =====================

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        log.info("JS click performed on {}", element);
    }

    protected void jsType(WebElement element, String text) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + text + "';", element);
        log.info("JS set value '{}' for {}", text, element);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        log.info("Scrolled into view: {}", element);
    }

    // ===================== OVERLAY HANDLING =====================

    protected void closeOverlays() {
        try {
            List<WebElement> coachmarks = driver.findElements(By.xpath("//span[contains(@class,'coachmark')]"));
            if (!coachmarks.isEmpty()) {
                log.info("Overlay/coachmark detected, closing...");
                driver.findElement(By.tagName("body")).click();
                wait.until(ExpectedConditions.invisibilityOfAllElements(coachmarks));
            }
        } catch (Exception e) {
            log.info("No overlay present");
        }
    }

    // ===================== UTIL =====================

    protected boolean isDisplayed(WebElement element) {
        try {
            return waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}