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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {
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
    @DisplayName("Получение заказов авторизированного пользователя")
    public void getOrdersWithLoginPassed() {
        List<String> actualIngredients = OrderApi.getIngredients();
        Order order = new Order(Arrays.asList(actualIngredients.get(3), actualIngredients.get(4), actualIngredients.get(5)));
        Response createOrderResponse = OrderApi.createOrderWithLogin(order, userToken);
        createOrderResponse.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));

        Response getOrdersResponse = OrderApi.getOrdersWithLogin(userToken);
        getOrdersResponse.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов неавторизированного пользователя")
    public void getOrdersWithoutLoginFailed() {
        List<String> actualIngredients = OrderApi.getIngredients();
        Order order = new Order(Arrays.asList(actualIngredients.get(3), actualIngredients.get(4), actualIngredients.get(5)));
        Response createOrderResponse = OrderApi.createOrderWithLogin(order, userToken);
        createOrderResponse.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));

        Response getOrdersResponse = OrderApi.getOrdersWithoutLogin();
        getOrdersResponse.then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
