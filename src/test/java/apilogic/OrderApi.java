package apilogic;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Order;

import java.util.List;

import static constants.ConstantsApi.*;
import static io.restassured.RestAssured.given;

public class OrderApi {
    @Step("Создание заказа с авторизацией")
    public static Response createOrderWithLogin(Order order, String userToken) {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", userToken)
                .body(order)
                .when()
                .post(ORDER_API_CREATE);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrderWithoutLogin(Order order) {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER_API_CREATE);
    }

    @Step("Получить заказы с авторизацией")
    public static Response getOrdersWithLogin(String userToken) {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", userToken)
                .when()
                .get(ORDER_API_GET_USER_ORDERS);
    }

    @Step("Получить заказы без авторизации")
    public static Response getOrdersWithoutLogin() {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_API_GET_USER_ORDERS);
    }

    @Step("Получить ингредиенты")
    public static List<String> getIngredients() {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_API_GET_INGREDIENTS)
                .then()
                .extract().path("data._id");
    }
}
