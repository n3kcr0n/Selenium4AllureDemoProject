package Framework.BaseTest;
import Framework.DriverFactory.DriverFactory;
import Framework.Utils.TestUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;

public class BaseTest implements ITestListener {
    //Used for parallel runs
    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeMethod
    public void testSetup(){
        WebDriver driver = new DriverFactory().initDriver();
        this.driver.set(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result){
        WebDriver currentDriver = driver.get();
        if(!result.isSuccess()){
            String caseName = result.getMethod().getMethodName();
            File destination = new File("allure-results"+File.separator+"screenshots"+File.separator+caseName+".png");
            TestUtils.takeScreenShots(driver.get(),destination);
        }
        if (currentDriver != null) {
            currentDriver.quit();
            driver.remove();
        }
    }
}
