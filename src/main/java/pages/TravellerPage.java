package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.Logger;
import utils.LoggerUtil;

public class TravellerPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private Logger log = LoggerUtil.getLogger(this.getClass());

    public TravellerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // ===================== Locators =====================

    private By travellerSection = By.xpath("//p[contains(text(),'Traveller Details')]");
    private By contactSection = By.xpath("//p[contains(text(),'Contact Details')]");
    private By fareSummary = By.xpath("//p[contains(text(),'Fare Summary')]");
    private By addTravellerBtn = By.xpath("//button[contains(.,'ADD NEW ADULT')]");
    private By totalAmount = By.xpath("//span[contains(text(),'₹')]");
    private By continueBtn = By.xpath("//button[contains(.,'Continue')]");
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
            log.warn("Click failed, using JS click");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    // ===================== VALIDATIONS =====================

    public boolean isTravellerPageLoaded() {
        try {
            log.info("Validating Traveller Page");

            waitForInvisibility(loader);

            waitForVisibility(travellerSection);
            waitForVisibility(contactSection);
            waitForVisibility(fareSummary);

            return true;

        } catch (Exception e) {
            log.error("Traveller page not loaded");
            return false;
        }
    }

    public boolean validateAllSections() {
        try {
            waitForVisibility(travellerSection);
            waitForVisibility(contactSection);
            waitForVisibility(fareSummary);

            log.info("All sections visible");
            return true;

        } catch (Exception e) {
            log.error("Section validation failed");
            return false;
        }
    }

    public boolean isFareDisplayed() {
        try {
            String fare = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(totalAmount)
            ).getText();

            log.info("Fare: {}", fare);

            return fare != null && !fare.trim().isEmpty();

        } catch (Exception e) {
            log.error("Fare not displayed");
            return false;
        }
    }

    public boolean validateFareFormat() {
        String fare = wait.until(ExpectedConditions.visibilityOfElementLocated(totalAmount)).getText();

        log.info("Fare format check: {}", fare);

        return fare.contains("₹");
    }

    public boolean isAddTravellerVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(addTravellerBtn)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isContinueEnabled() {
        WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(continueBtn));
        return btn.isEnabled();
    }

    // ===================== ACTION =====================

    public void clickContinue() {

        log.info("Clicking Continue");

        waitForInvisibility(loader);

        try {
            safeClick(continueBtn);
        } catch (Exception e) {
            log.error("Failed to click Continue");
            throw e;
        }
    }
}