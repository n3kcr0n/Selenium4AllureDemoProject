package TestCases;

import Framework.BaseTest.BaseTest;
import Framework.Models.UserModel;
import Framework.PageObjects.RegistrationPage;
import Framework.PageObjects.TodoPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegisterUserUiTest extends BaseTest {

    @Test(description = "Should be able to register via UI.")
    public void registerUser() {
        UserModel userTest1 = new UserModel();
        RegistrationPage.getInstance().loadPage(driver.get());
        RegistrationPage.getInstance().register(
                driver.get(),
                userTest1.getFirstName(),
                userTest1.getLastName(),
                userTest1.getEmail(),
                userTest1.getPassword());

        Assert.assertTrue(TodoPage.getInstance().isWelcomeMessageDisplayed(driver.get()));

    }
}
