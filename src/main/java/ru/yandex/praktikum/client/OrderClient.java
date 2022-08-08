package ru.yandex.praktikum.client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.model.Order;
import static io.restassured.RestAssured.given;
public class OrderClient extends BaseClient {
    @Step("Creating an order without authorization")
    public Response createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getReqSpec())
                .body(order)
                .when()
                .post(BASE_URL + "/api/orders")
                .then().log().all().extract().response();
    }
    @Step("Creating an order with authorization")
    public Response createOrderWithAuthorization(Order order, String token) {
        return given()
                .header("Authorization", token)
                .spec(getReqSpec())
                .body(order)
                .when()
                .post(BASE_URL + "/api/orders")
                .then().log().all().extract().response();
    }
}
