package base;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import utils.*;

import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class BaseTest {

    protected WebDriver driver;
    protected Logger log = LoggerUtil.getLogger(this.getClass());
    protected ExtentTest test;

    @BeforeMethod
    public void setup(Method method) {

        log.info("========== START TEST: {} ==========", method.getName());

        driver = DriverFactory.initDriver();

        String url = ConfigReader.get("baseUrl");
        driver.get(url);

        log.info("Navigated to URL: {}", url);

        // Extent Test Init
        test = ExtentManager.getInstance().createTest(method.getName());
        test.info("Test started: " + method.getName());
    }

    @AfterMethod
    public void teardown(ITestResult result) {

        if (result.getStatus() == ITestResult.FAILURE) {

            log.error("Test FAILED: {}", result.getName());
            log.error("Reason: ", result.getThrowable());

            test.fail(result.getThrowable());

            String path = ScreenshotUtil.capture(driver, result.getName());
            test.addScreenCaptureFromPath(path);

        } else if (result.getStatus() == ITestResult.SUCCESS) {

            log.info("Test PASSED: {}", result.getName());
            test.pass("Test passed");

        } else {

            log.warn("Test SKIPPED: {}", result.getName());
            test.skip("Test skipped");
        }

        DriverFactory.quitDriver();
        log.info("========== END TEST ==========\n");
    }

    @AfterSuite
    public void flushReport() {
        ExtentManager.flushReport();
        log.info("Extent report flushed");
    }
}