package ru.yandex.praktikum;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.model.Order;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.client.UserClient;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.praktikum.model.User.getRandomUser;

public class CreateOrderTest {
    User user;
    Order order;
    String token;
    OrderClient orderClient;
    UserClient userClient;
    @Test
    @DisplayName("Creating an order with authorization")
    public void createOrderWithAuthorization() {
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa70"});
        Response responseCreateOrder = orderClient.createOrderWithAuthorization(order, token);
        assertEquals(SC_OK, responseCreateOrder.statusCode());
        assertTrue(responseCreateOrder.body().jsonPath().getBoolean("success"));
    }
    @Test
    @DisplayName("Creating an order without authorization")
    public void createOrderWithoutAuthorization() {
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa70"});
        Response responseCreateOrder = orderClient.createOrderWithoutAuthorization(order);
        assertEquals(SC_OK, responseCreateOrder.statusCode());
        assertTrue(responseCreateOrder.body().jsonPath().getBoolean("success"));
    }
    @Test
    @DisplayName("Creating an order without ingredients")
    public void createOrderWithoutIngredient() {
        order = new Order(new String[]{});
        Response responseCreateOrder = orderClient.createOrderWithAuthorization(order, token);
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());
        assertEquals("Ingredient ids must be provided", responseCreateOrder.body().jsonPath().getString("message"));
    }
    @Test
    @DisplayName("Creating an order with ingredients")
    public void createOrderWithIngredient() {
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa70"});
        Response responseCreateOrder = orderClient.createOrderWithAuthorization(order, token);
        assertEquals(SC_OK, responseCreateOrder.statusCode());
        assertTrue(responseCreateOrder.body().jsonPath().getBoolean("success"));
    }
    @Test
    @DisplayName("Creating an order with an invalid ingredient hash and without authorization")
    public void createOrderWithWrongHashIngredientAuth() {
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c+0", "61c0c5a71d1f82001bdaaa6d+0"});
        Response responseCreateOrder = orderClient.createOrderWithoutAuthorization(order);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());
    }
    @Test
    @DisplayName("Creating an order with an invalid ingredient hash and with authorization")
    public void createOrderWithWrongHashIngredientWithoutAuth() {
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c+0", "61c0c5a71d1f82001bdaaa6d+0"});
        Response responseCreateOrder = orderClient.createOrderWithAuthorization(order, token);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());
    }
    @Before
    public void setup() {
        user = getRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        Response responseCreateUser = userClient.createUser(user);
        token = responseCreateUser.body().jsonPath().getString("accessToken");
    }
    @After
    public void delete() {
        if (token != null) {
            userClient.deleteUser(token);
        }
    }
}
