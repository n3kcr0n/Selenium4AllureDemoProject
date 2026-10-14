package Framework.Api;

import Framework.Models.UserModel;
import Framework.Utils.ConfigUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jspecify.annotations.NonNull;

import static io.restassured.RestAssured.given;

public class UserApi {
    private static UserApi userApi;
    private UserApi(){}
    public static UserApi getInstance(){
        if(userApi == null){
            userApi = new UserApi();
        }
        return userApi;
    }


    public Response registerUser(UserModel user){
         return  given()
                .baseUri(ConfigUtils.getInstance().getBaseUrl())
                .contentType(ContentType.JSON)
                .body(user)
        .when()
                .post("/api/v1/users/register")
        .then()
                .extract().response();

    }

    public Response createTodo(@NonNull UserModel user, String name){
         return given()
                    .baseUri(ConfigUtils.getInstance().getBaseUrl())
                    .contentType(ContentType.JSON)
                    .body("{\"item\":\""+name+"\",\"isCompleted\":false}")
                    .auth().oauth2(user.getToken())
                .when()
                    .post("/api/v1/tasks")
                    .then()
                .extract().response();
    }
}
