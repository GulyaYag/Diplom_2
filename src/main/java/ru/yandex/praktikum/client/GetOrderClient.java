package ru.yandex.praktikum.client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class GetOrderClient extends BaseClient {
    @Step("Getting a list of orders without authorization")
    public Response receiveOrdersWithoutAuthorization() {
        return given()
                .spec(getReqSpec())
                .get(BASE_URL + "/api/orders");
    }
    @Step("Getting a list of orders with authorization")
    public Response receiveOrdersWithAuthorization(String token) {
        return given()
                .header("Authorization", token)
                .spec(getReqSpec())
                .get(BASE_URL + "/api/orders");
    }
}
