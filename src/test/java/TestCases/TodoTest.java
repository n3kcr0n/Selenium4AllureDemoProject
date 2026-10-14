package TestCases;

import Framework.BaseTest.BaseTest;
import Framework.Models.UserModel;
import Framework.PageObjects.CreatNewTodoPage;
import Framework.PageObjects.RegistrationPage;
import Framework.PageObjects.TodoPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TodoTest extends BaseTest {

    @Test(description = "Should be able to create to do via UI.")
    public void createTodo() {
        RegistrationPage.getInstance().loadPage(driver.get());
        UserModel userTest2 = new UserModel();
        RegistrationPage.getInstance().register(driver.get(), userTest2.getFirstName(), userTest2.getLastName(), userTest2.getEmail(), userTest2.getPassword());

        TodoPage.getInstance().clickAddTodoButton(driver.get());
        CreatNewTodoPage.getInstance().createTodo(driver.get(), "TESTING To do");

    }

    @Test(description = "Should be able to delete to do via UI.")
    public void deleteTodo() {
        RegistrationPage.getInstance().loadPage(driver.get());
        UserModel userTest2 = new UserModel();
        RegistrationPage.getInstance().register(driver.get(), userTest2.getFirstName(), userTest2.getLastName(), userTest2.getEmail(), userTest2.getPassword());

        TodoPage.getInstance().clickAddTodoButton(driver.get());
        CreatNewTodoPage.getInstance().createTodo(driver.get(), "Selenium 4");
        boolean isDisplayed = CreatNewTodoPage.getInstance().isTodoDisplayed(driver.get(), "Selenium 4");
        Assert.assertTrue(isDisplayed);
        CreatNewTodoPage.getInstance().deleteTodo(driver.get(), "Selenium 4");
        Assert.assertTrue(CreatNewTodoPage.getInstance().isNoTodoMessageDisplayed(driver.get()));
    }
}
