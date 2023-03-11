package user;

import apilogic.UserApi;
import apilogic.UserCredentials;
import apilogic.UserData;
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

public class UpdateUserTest {

    private User user;
    private String userToken;

    private final static String EMAIL = "new_email@mail.ru";
    private final static String NAME = "New Name User";

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
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void updateUserWithLoginPassed() {
        Response loginUserResponse = UserApi.loginUser(new UserCredentials(user.getEmail(), user.getPassword()));
        loginUserResponse.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        Response updateUserResponse = UserApi.patchInfoAboutUserWithLogin(userToken, new UserData(EMAIL, NAME));
        updateUserResponse.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(EMAIL))
                .body("user.name", equalTo(NAME));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void updateUserWithoutLoginPassed() {
        Response updateUserResponse = UserApi.patchInfoAboutUserWithoutLogin(new UserData(EMAIL, NAME));
        updateUserResponse.then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
