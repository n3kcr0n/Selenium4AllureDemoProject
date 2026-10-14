package TestCases;

import Framework.Api.UserApi;
import Framework.Models.UserModel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegisterUserApiTest {
    @Test(description = "Should be able register via API.")
    public void registerUserViaApi(){
        UserModel userTest2 = new UserModel();
        Response response = UserApi.getInstance().registerUser(userTest2);
        Assert.assertEquals(response.statusCode(),201);

    }
}
