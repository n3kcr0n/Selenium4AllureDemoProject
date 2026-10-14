package Framework.DriverFactory;

import Framework.Utils.ConfigUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Locale;

public class DriverFactory {

    public WebDriver initDriver() {

        String browser = System.getProperty(
                "browser",
                ConfigUtils.getInstance().getProperty("BROWSER")
        );

        String headlessProperty =
                ConfigUtils.getInstance().getProperty("HEADLESS");

        boolean headless = Boolean.parseBoolean(headlessProperty);

        WebDriver driver;

        switch (browser.toUpperCase(Locale.ROOT)) {

            case "CHROME" -> {
                ChromeOptions options = new ChromeOptions();

                if (headless) {
                    options.addArguments("--headless=new");
                }

                driver = new ChromeDriver(options);
            }

            case "FIREFOX" -> {
                FirefoxOptions options = new FirefoxOptions();

                if (headless) {
                    options.addArguments("-headless");
                }

                driver = new FirefoxDriver(options);
            }

            case "EDGE" -> {
                EdgeOptions options = new EdgeOptions();

                if (headless) {
                    options.addArguments("--headless=new");
                }

                driver = new EdgeDriver(options);
            }

            default -> throw new RuntimeException(
                    "Browser is not supported: " + browser
            );
        }

        if (!headless) {
            driver.manage().window().maximize();
        }

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(5));

        return driver;
    }
}
