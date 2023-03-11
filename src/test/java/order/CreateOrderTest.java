package order;

import apilogic.OrderApi;
import apilogic.UserApi;
import apilogic.UserGenerator;
import constants.ConstantsApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
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
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithLoginPassed() {
        List<String> actualIngredients = OrderApi.getIngredients();
        Order order = new Order(Arrays.asList(actualIngredients.get(3), actualIngredients.get(4), actualIngredients.get(5)));
        Response response = OrderApi.createOrderWithLogin(order, userToken);
        response.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithOutLoginPassed() {
        List<String> actualIngredients = OrderApi.getIngredients();
        Order order = new Order(Arrays.asList(actualIngredients.get(3), actualIngredients.get(4), actualIngredients.get(5)));
        Response response = OrderApi.createOrderWithoutLogin(order);
        response.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsFailed(){
        Order order = new Order(new ArrayList());
        Response response = OrderApi.createOrderWithLogin(order, userToken);
        response.then()
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithWrongIngredientsFailed() {
        Order order = new Order(Arrays.asList("-100", "jhhh", "lkj890"));
        Response response = OrderApi.createOrderWithLogin(order, userToken);
        response.then()
                .assertThat()
                .statusCode(500);
    }

}
