package Framework.PageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TodoPage {
    //Instance of class to be used in singleton pattern
    private static TodoPage todoPage;

    //Constructor with singleton Pattern
    private TodoPage(){};
    public static TodoPage getInstance(){
        if(todoPage == null){
            todoPage = new TodoPage();
        }
        return todoPage;
    }

    //Elements
    private final By addTodo_Btn = new By.ByCssSelector("[data-testid=\"add\"]");
    private final By welcome_Msg = new By.ByCssSelector("[data-testid=\"welcome\"]");

    //Methods
    @Step("Load the todo Page")
    public void loadPage(WebDriver driver){
        driver.get("https://todo.qacart.com/todo");
    }

    @Step("Click the add to do button")
    public void clickAddTodoButton(WebDriver driver){
        driver.findElement(addTodo_Btn).click();
    }

    @Step("Verify the welcome message")
    public boolean isWelcomeMessageDisplayed(WebDriver driver){
        return driver.findElement(welcome_Msg).isDisplayed();
    }
}
