package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.*;

public class TestListener implements ITestListener {

    static ExtentReports extent = ExtentManager.getInstance();
    static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        extent.flush();
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}