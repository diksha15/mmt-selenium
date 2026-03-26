package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtil;

import java.time.Duration;
import java.util.List;

public class FlightResultsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private Logger log = LoggerUtil.getLogger(this.getClass());

    public FlightResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // ===================== Locators =====================

    private By resultsLoaded = By.xpath("//div[contains(@class,'listingCard')]");
    private By allFlights = By.xpath("//div[contains(@class,'listingCard')]");
    private By bookNowButtons = By.xpath("//button[contains(.,'Book')]");
    private By priceElements = By.xpath("//span[contains(text(),'₹')]");
    private By firstBookNowBtn = By.xpath("(//button[contains(.,'Book')])[1]");
    private By loader = By.xpath("//div[contains(@class,'loader')]");

    // ===================== Utility =====================

    private void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private List<WebElement> waitForAll(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    private void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    private void safeClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            log.warn("Click intercepted, using JS click");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    // ===================== VALIDATIONS =====================

    public boolean isPageLoaded() {
        try {
            log.info("Validating Flight Results Page");

            waitForInvisibility(loader);
            waitForVisibility(resultsLoaded);

            return true;

        } catch (Exception e) {
            log.error("Results page not loaded");
            return false;
        }
    }

    public boolean hasFlights() {
        int count = waitForAll(allFlights).size();
        log.info("Flight count: {}", count);
        return count > 0;
    }

    public boolean validateBookNowButtons() {
        int flights = waitForAll(allFlights).size();
        int buttons = waitForAll(bookNowButtons).size();

        log.info("Flights: {}, Book buttons: {}", flights, buttons);

        return buttons > 0 && buttons <= flights;
    }

    public boolean isPriceDisplayed() {
        List<WebElement> prices = waitForAll(priceElements);
        log.info("Price elements found: {}", prices.size());
        return prices.size() > 0;
    }

    // ===================== ACTION =====================

    public void selectFirstFlight() {

        log.info("Selecting first flight");

        waitForInvisibility(loader);
        waitForVisibility(resultsLoaded);

        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(firstBookNowBtn));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

            safeClick(firstBookNowBtn);

            log.info("Clicked on first Book Now button");

        } catch (Exception e) {
            log.error("Failed to select first flight");
            throw e;
        }
    }
}