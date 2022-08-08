package ru.yandex.praktikum;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.GetOrderClient;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.Order;
import ru.yandex.praktikum.model.User;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;
import static ru.yandex.praktikum.model.User.getRandomUser;

public class ReceiveOrderTest {
    User user;
    Order order;
    String token;
    OrderClient orderClient;
    UserClient userClient;
    GetOrderClient getOrderClient;
    @Test
    @DisplayName("Receiving user orders with authorization")
    public void receiveOrdersWithAuthorization() {
        getOrderClient = new GetOrderClient();
        Response responseReceiveOrder = getOrderClient.receiveOrdersWithAuthorization(token);
        assertTrue(responseReceiveOrder.body().jsonPath().getBoolean("success"));
        assertEquals(SC_OK, responseReceiveOrder.statusCode());
    }
    @Test
    @DisplayName("Receiving user orders without authorization")
    public void receiveOrdersWithoutAuthorization() {
        getOrderClient = new GetOrderClient();
        Response responseReceiveOrder = getOrderClient.receiveOrdersWithoutAuthorization();
        assertFalse(responseReceiveOrder.body().jsonPath().getBoolean("success"));
        assertEquals(SC_UNAUTHORIZED, responseReceiveOrder.statusCode());
        assertEquals("You should be authorised", responseReceiveOrder.body().jsonPath().getString("message"));
    }
    @Before
    public void setup() {
        user = getRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        Response responseCreateUser = userClient.createUser(user);
        token = responseCreateUser.body().jsonPath().getString("accessToken");
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa6d"});
    }
    @After
    public void delete() {
        if (token != null) {
            userClient.deleteUser(token);
        }
    }
}
