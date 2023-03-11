package apilogic;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.User;

import static constants.ConstantsApi.*;
import static io.restassured.RestAssured.given;

public class UserApi {

    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .post(USER_API_CREATE);
    }

    @Step("Авторизация пользователя в системе")
    public static Response loginUser(UserCredentials credentials) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(credentials)
                .when()
                .post(USER_API_LOGIN);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String userToken) {
        return given()
                .header("Authorization", userToken)
                .contentType(ContentType.JSON)
                .and()
                .when()
                .delete(USER_API_DELETE);
    }

    @Step ("Изменение информации о пользователе с авторизацией")
    public static Response patchInfoAboutUserWithLogin(String userToken, UserData userData) {
        return given()
                .header("authorization", userToken)
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .patch(USER_API_PATCH_INFO);
    }

    @Step ("Изменение информации о пользователе без авторизации")
    public static Response patchInfoAboutUserWithoutLogin(UserData userData) {
        return given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .patch(USER_API_PATCH_INFO);
    }
}
