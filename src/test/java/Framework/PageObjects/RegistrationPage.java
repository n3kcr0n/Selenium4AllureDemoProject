package Framework.PageObjects;

import Framework.Api.UserApi;
import Framework.Models.UserModel;
import Framework.Utils.ConfigUtils;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class RegistrationPage {
    //Instance of class to be used in singleton pattern
    private static RegistrationPage registrationPage;
    //Elements
    private final By firstName_field = new By.ByCssSelector("[data-testid=\"first-name\"]");
    private final By lastName_field = new By.ByCssSelector("[data-testid=\"last-name\"]");
    private final By email_field = new By.ByCssSelector("[data-testid=\"email\"]");
    private final By password_field = new By.ByCssSelector("[data-testid=\"password\"]");
    private final By confirmPassword_field = new By.ByCssSelector("[data-testid=\"confirm-password\"]");
    private final By submit_Btn = new By.ByCssSelector("[data-testid=\"submit\"]");
    //Constructor with singleton Pattern
    private RegistrationPage() {
    }

    public static RegistrationPage getInstance() {
        if (registrationPage == null) {
            registrationPage = new RegistrationPage();
        }
        return registrationPage;
    }

    //Methods-Steps
    @Step("Load the sign up page")
    public void loadPage(WebDriver driver) {
        driver.get(ConfigUtils.getInstance().getBaseUrl() + "/signup");
    }

    @Step("Register user via UI")
    public void register(WebDriver driver, String name, String lastName, String email, String pw) {
        driver.findElement(firstName_field).sendKeys(name);
        driver.findElement(lastName_field).sendKeys(lastName);
        driver.findElement(email_field).sendKeys(email);
        driver.findElement(password_field).sendKeys(pw);
        driver.findElement(confirmPassword_field).sendKeys(pw);
        driver.findElement(submit_Btn).click();
    }

    @Step("Register user via api and inject the token to browser cookies")
    public void registerViaApiAndInjectToCookies(WebDriver driver, UserModel user) {
        Response response = UserApi.getInstance().registerUser(user);

        String token = response.path("access_token");
        String id = response.path("userID");
        String fname = response.path("firstName");
        user.setToken(token);

        Cookie accessTokenCookie = new Cookie("access_token", token);
        Cookie userIdCookie = new Cookie("userID", id);
        Cookie firstNameCookie = new Cookie("firstName", fname);

        //injecting the cookie to browser
        RegistrationPage.getInstance().loadPage(driver);
        driver.manage().addCookie(accessTokenCookie);
        driver.manage().addCookie(userIdCookie);
        driver.manage().addCookie(firstNameCookie);
        driver.navigate().refresh();
    }
}
