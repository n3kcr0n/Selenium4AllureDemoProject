package TestCases;

import Framework.Api.UserApi;
import Framework.BaseTest.BaseTest;
import Framework.Models.UserModel;
import Framework.PageObjects.CreatNewTodoPage;
import Framework.PageObjects.RegistrationPage;
import Framework.PageObjects.TodoPage;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MixedUIApiTest extends BaseTest {

    @Test(description = "Should be able to register via API and delete a todo.")
    public void registerViaApiAndDeleteTodo(){
        UserModel user = new UserModel();
        RegistrationPage.getInstance().registerViaApiAndInjectToCookies(driver.get(),user);
        TodoPage.getInstance().clickAddTodoButton(driver.get());
        CreatNewTodoPage.getInstance().createTodo(driver.get(), "Selenium 4");
        boolean isDisplayed = CreatNewTodoPage.getInstance().isTodoDisplayed(driver.get(), "Selenium 4");
        Assert.assertTrue(isDisplayed);
        CreatNewTodoPage.getInstance().deleteTodo(driver.get(), "Selenium 4");
        Assert.assertTrue(CreatNewTodoPage.getInstance().isNoTodoMessageDisplayed(driver.get()));
    }

    @Test(description = "Should be able to create to do via Api and view it from the Ui.")
    public void createTodoViaApi() {
        UserModel user2 = new UserModel();
        RegistrationPage.getInstance().registerViaApiAndInjectToCookies(driver.get(),user2);
        TodoPage.getInstance().loadPage(driver.get());
        Response res = CreatNewTodoPage.getInstance().createTodoViaApi(user2,"Selenium 4");
        Assert.assertEquals(res.statusCode(),201);
        TodoPage.getInstance().loadPage(driver.get());
        boolean isDisplayed = CreatNewTodoPage.getInstance().isTodoDisplayed(driver.get(), "Selenium 4");
        Assert.assertTrue(isDisplayed);
    }
}
