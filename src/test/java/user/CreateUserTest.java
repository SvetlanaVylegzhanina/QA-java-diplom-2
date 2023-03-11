package user;

import apilogic.UserApi;
import apilogic.UserCredentials;
import apilogic.UserGenerator;
import constants.ConstantsApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = ConstantsApi.URL_STELLAR_BURGERS;
    }

    @Test
    @DisplayName("Создание пользователя со всеми полями")
    public void createNewUserWithAllFieldsPassed() {
        User user = UserGenerator.getRandomUserWithAllFields();
        Response response = UserApi.createUser(user);

        response.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
        String userToken = response.path("accessToken");

        UserApi.loginUser(new UserCredentials(user.getEmail(), user.getPassword()));
        UserApi.deleteUser(userToken);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void getRandomUserWithoutPasswordFailed() {
        User user = UserGenerator.getRandomUserWithoutPassword();
        Response response = UserApi.createUser(user);

        response.then()
                .statusCode(403)
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void getRandomUserWithoutEmailFailed() {
        User user = UserGenerator.getRandomUserWithoutEmail();
        Response response = UserApi.createUser(user);

        response.then()
                .statusCode(403)
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя с существующим логином")
    public void createAlreadyExistsUserFailed() {
        User user = UserGenerator.getRandomUserWithAllFields();
        Response response1 = UserApi.createUser(user);

        response1.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));

        Response response2 = UserApi.createUser(user);

        response2.then()
                .statusCode(403)
                .assertThat()
                .body("message", equalTo("User already exists"));

        String userToken = response1.path("accessToken");
        UserApi.loginUser(new UserCredentials(user.getEmail(), user.getPassword()));
        UserApi.deleteUser(userToken);
    }


}
