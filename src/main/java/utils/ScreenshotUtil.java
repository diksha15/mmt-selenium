package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String capture(WebDriver driver, String testName) {

        // Create timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Screenshot folder
        String folderPath = System.getProperty("user.dir") + "/reports/screenshots/";
        new File(folderPath).mkdirs(); // create if not exists

        // File path
        String filePath = folderPath + testName + "_" + timestamp + ".png";

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);

            FileUtils.copyFile(source, destination);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}