package ru.yandex.praktikum.client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.model.User;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
public class UserClient extends BaseClient {
    @Step("Create user")
    public Response createUser(User user) {
        return given()
                .spec(getReqSpec())
                .body(user)
                .when()
                .post(BASE_URL + "/api/auth/register");
    }
    @Step("Login user")
    public Response loginUser(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getReqSpec())
                .body(user)
                .when()
                .post(BASE_URL + "/api/auth/login");
    }
    @Step("Changing user data without authorization")
    public Response changeUserDataWithoutAuthorization(User user) {
        return given()
                .spec(getReqSpec())
                .body(user)
                .when()
                .patch(BASE_URL + "/api/auth/user");
    }
    @Step("Changing user data with authorization")
    public Response changeUserDataWithAuthorization(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getReqSpec())
                .body(user)
                .when()
                .patch(BASE_URL + "/api/auth/user");
    }
    @Step("Удаление пользователя")
    public Boolean deleteUser(String token) {
        return given()
                .spec(getReqSpec())
                .header("Authorization", token)
                .when()
                .delete(BASE_URL + "/api/auth/user")
                .then().log().all()
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .extract()
                .path("ok");
    }
}
