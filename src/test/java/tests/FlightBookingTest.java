package tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.*;

import java.time.Duration;

public class FlightBookingTest extends BaseTest {

    @Test
    public void bookFlightTillTravellerPage() throws InterruptedException {

        FlightSearchPage search = new FlightSearchPage(driver);
        Thread.sleep(6000);

       Actions actions = new Actions(driver);
       actions.moveByOffset(200, 200).pause(Duration.ofSeconds(1)).perform();

        // ===================== SEARCH PAGE =====================
        Assert.assertTrue(search.isPageLoaded(), "Flight Search page not loaded");
        test.info("Flight Search page loaded");

        ( (JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)");

        search.closeLoginPopup();Thread.sleep(6000);
        search.handleAIChatBox();Thread.sleep(6000);

        // Select cities
        search.selectFromCity("New Delhi, India");Thread.sleep(3000);
        search.selectToCity("Mumbai");Thread.sleep(3000);

        // Validate search button
        Assert.assertTrue(search.isSearchButtonVisible(), "Search button is not visible");
        test.info("Search button is visible");
        Thread.sleep(6000);
        // Select dateThread.sleep(6000);
        search.openCalendar();
        search.selectDepartureDate("Mar", "30", "2026");

        // Verify selected date
        Thread.sleep(6000);

        search.clickSearch();

        // ===================== RESULTS PAGE =====================
        FlightResultsPage results = new FlightResultsPage(driver);

        Assert.assertTrue(results.isPageLoaded(), "Flight Results page not loaded");
        test.info("Flight Results page loaded");

        // Validate first flight button is visible

        // Select first flight
        results.selectFirstFlight();
        test.info("Selected first available flight");
        Thread.sleep(6000);
        // ===================== TRAVELLER PAGE =====================
        TravellerPage traveller = new TravellerPage(driver);

        // Validate traveller page fully loaded
        Assert.assertTrue(traveller.isTravellerPageLoaded(), "Traveller page not loaded");
        test.pass("Traveller page loaded successfully");

        // Validate fare is displayed
        Assert.assertTrue(traveller.isFareDisplayed(), "Fare not displayed on Traveller page");
        test.info("Fare displayed correctly");

        // Validate add traveller button
        Assert.assertTrue(traveller.isAddTravellerVisible(), "'Add Traveller' button not visible");
        test.info("'Add Traveller' button visible");

        // Continue to next page
        traveller.clickContinue();
        test.info("Clicked Continue on Traveller page");
    }
}