package Framework.Utils;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public static void takeScreenShots(WebDriver driver, File fileDestination) {
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file,fileDestination);
            InputStream is = new FileInputStream(fileDestination);
            Allure.attachment("screenshot",is);
        }catch (IOException e){
            throw new RuntimeException(e.getCause());
        }
    }
}
