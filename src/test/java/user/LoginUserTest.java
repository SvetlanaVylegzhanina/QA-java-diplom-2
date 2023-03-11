package user;

import apilogic.UserApi;
import apilogic.UserCredentials;
import apilogic.UserGenerator;
import constants.ConstantsApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {

    private User user;
    private String userToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = ConstantsApi.URL_STELLAR_BURGERS;

        user = UserGenerator.getRandomUserWithAllFields();
        Response createUserResponse = UserApi.createUser(user);
        createUserResponse.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
        userToken = createUserResponse.path("accessToken");
    }

    @After
    public void clearAfter() {
        UserApi.deleteUser(userToken);
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void loginUserPassed() {
        Response loginUserResponse = UserApi.loginUser(new UserCredentials(user.getEmail(), user.getPassword()));
        loginUserResponse.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизация без email")
    public void loginUserWithoutEmailFailed() {
        Response loginUserResponse = UserApi.loginUser(new UserCredentials("", user.getPassword()));
        loginUserResponse.then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void loginUserWithoutPasswordFailed() {
        Response loginUserResponse = UserApi.loginUser(new UserCredentials(user.getEmail(), ""));
        loginUserResponse.then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    public void loginUserWithIncorrectEmailFailed() {
        Response loginUserResponse = UserApi.loginUser(new UserCredentials("Incorrect_email@test.ru", user.getPassword()));
        loginUserResponse.then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginUserWithIncorrectPasswordFailed() {
        Response loginUserResponse = UserApi.loginUser(new UserCredentials(user.getEmail(), "Incorrect@!123"));
        loginUserResponse.then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
