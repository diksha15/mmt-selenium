package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import utils.LoggerUtil;

import java.util.List;

import org.apache.logging.log4j.Logger;

public class FlightSearchPage extends BasePage {

    private Logger log = LoggerUtil.getLogger(this.getClass());

    public FlightSearchPage(WebDriver driver) {
        super(driver);  // Call BasePage constructor
        PageFactory.initElements(driver, this);
    }

    // ===================== WebElements =====================

    @FindBy(id = "fromCity")
    private WebElement fromCity;

    @FindBy(id = "toCity")
    private WebElement toCity;

    @FindBy(xpath = "//input[@placeholder='From']")
    private WebElement fromInput;

    @FindBy(xpath = "//input[@placeholder='To']")
    private WebElement toInput;

    @FindBy(xpath = "//a[text()='Search']")
    private WebElement searchBtn;

    @FindBy(xpath = "//label[@for='departure']")
    private WebElement calendarOpen;

    @FindBy(xpath = "//button[@aria-label='Next Month']")
    private WebElement nextMonthBtn;

    @FindBy(xpath = "//span[@data-cy='closeModal']")
    private List<WebElement> loginPopupClose;

    @FindBy(xpath = "//span[contains(@class,'coachmark')]")
    private List<WebElement> coachmark;

    @FindBy(tagName = "body")
    private WebElement body;

    // ===================== Validations =====================

    public boolean isPageLoaded() {
        try {
            log.info("Validating Flight Search Page");
            waitForLoaderToDisappear();
            return isDisplayed(fromCity);
        } catch (Exception e) {
            log.error("Search page not loaded", e);
            return false;
        }
    }

    public boolean isSearchButtonVisible() {
        return isDisplayed(searchBtn);
    }

    // ===================== Actions =====================

    public void closeLoginPopup() {
        try {
            if (!loginPopupClose.isEmpty()) {
                safeClick(loginPopupClose.get(0));
                log.info("Login popup closed");
            }
        } catch (Exception e) {
            log.info("Login popup not present");
        }
    }

    public void handleAIChatBox() {
        try {
            driver.switchTo().frame(0);
            WebElement closeBtn = driver.findElement(By.xpath("//button[contains(@class,'close')]"));
            safeClick(closeBtn);
            driver.switchTo().defaultContent();
            log.info("AI Chatbox closed");
        } catch (Exception e) {
            log.info("AI Chatbox not present");
            driver.switchTo().defaultContent();
        }
    }

    public void selectFromCity(String city) {
        log.info("Selecting FROM city: {}", city);
        safeClick(fromCity);
        safeType(fromInput, city);
        WebElement cityOption = driver.findElement(
                By.xpath("//div[@class='revampedSuggestionHeader']//*[contains(text(),'" + city + "')]"));
        safeClick(cityOption);
    }

    public void selectToCity(String city) {
        log.info("Selecting TO city: {}", city);
        safeClick(toCity);
        safeType(toInput, city);
        WebElement cityOption = driver.findElement(
                By.xpath("//div[@class='revampedSuggestionHeader']//*[contains(text(),'" + city + "')]"));
        safeClick(cityOption);
    }

    public void openCalendar() {
        safeClick(calendarOpen);
        log.info("Calendar opened");
    }

    public void selectDepartureDate(String month, String day, String year) {
        log.info("Selecting date: {}-{}-{}", day, month, year);
        openCalendar();

        while (true) {
            WebElement monthElement = driver.findElement(By.xpath("//div[contains(@class,'Month')]"));
            if (monthElement.getText().contains(month)) break;
            safeClick(nextMonthBtn);
        }

        WebElement dateElement = driver.findElement(
                By.xpath("//div[contains(@aria-label,'" + month + " " + day + " " + year + "')]"));
        safeClick(dateElement);
        log.info("Date selected successfully");
    }

    public String getSelectedDate() {
        WebElement date = driver.findElement(By.xpath("//p[@data-cy='departureDate']"));
        String value = date.getText();
        log.info("Selected date: {}", value);
        return value;
    }

    public void clickSearch() {
        safeClick(searchBtn);
        log.info("Clicked on Search button");
    }
}