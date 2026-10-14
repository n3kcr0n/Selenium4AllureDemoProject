package Framework.PageObjects;

import Framework.Api.UserApi;
import Framework.Models.UserModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class CreatNewTodoPage {
    //Instance of class to be used in singleton pattern
    private static CreatNewTodoPage createNewTodoPage;

    //Constructor with singleton Pattern
    private CreatNewTodoPage(){};
    public static CreatNewTodoPage getInstance(){
        if(createNewTodoPage == null){
            createNewTodoPage = new CreatNewTodoPage();
        }
        return createNewTodoPage;
    }

    //Elements
    private final By createTodo_Field = new By.ByCssSelector("[data-testid=\"new-todo\"]");
    private final By createTodo_Btn = new By.ByCssSelector("[data-testid=\"submit-newTask\"]");
    private final By todo_Items = new By.ByCssSelector("[data-testid=\"todo-item\"]");
    private final By todoDelete_Icon = new By.ByCssSelector("[data-testid=\"delete\"]");
    private final By noTodo_msg = new By.ByCssSelector("[data-testid=\"no-todos\"]");

    //Methods
    @Step("Create to do via API")
    public void createTodo(WebDriver driver, String todoName) {
        driver.findElement(createTodo_Field).sendKeys(todoName);
        driver.findElement(createTodo_Btn).click();
    }

    @Step("Create to do via API")
    public Response createTodoViaApi(UserModel user, String todoName){
        return (Response) UserApi.getInstance().createTodo(user, todoName);
    }

    @Step("Verify if to do is displayed")
    public boolean isTodoDisplayed(WebDriver driver, String todoName) {
        return driver.findElements(todo_Items)
                .stream()
                .anyMatch(todo -> todo.getText().equals(todoName));
    }

    @Step("Delete a single to do")
    public void deleteTodo(WebDriver driver, String todoName) {
        WebElement todo = driver.findElements(todo_Items)
                .stream()
                .filter(item -> item.getText().contains(todoName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Todo item not found: " + todoName));

        todo.findElement(todoDelete_Icon).click();
    }

    @Step("Verify the NO todo message is displayed")
    public boolean isNoTodoMessageDisplayed(WebDriver driver) {
        return driver.findElement(noTodo_msg).isDisplayed();
    }
}
