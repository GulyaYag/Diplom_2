package ru.yandex.praktikum;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.User;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.model.User.getRandomUser;

public class ChangeDataTest{
    User user;
    String token;
    UserClient userClient = new UserClient();
    @Test
    @DisplayName("Change username with authorization")
    public void changeNameWithAuthorization() {
        Response responseCreateUser = userClient.createUser(user);
        token = responseCreateUser.body().jsonPath().getString("accessToken");
        user.setName("Gomer");
        Response responseChangeName = userClient.changeUserDataWithAuthorization(user, token);
        assertEquals(SC_OK, responseChangeName.statusCode());
        assertEquals("Gomer", responseChangeName.body().jsonPath().getString("user.name"));
    }
    @Test
    @DisplayName("Changing the user login with authorization")
    public void changeLoginWithAuthorization() {
        Response responseCreate = userClient.createUser(user);
        token = responseCreate.body().jsonPath().getString("accessToken");
        user.setEmail("gomer@test.ru");
        Response responseChangeLogin = userClient.changeUserDataWithAuthorization(user, token);
        assertEquals(SC_OK, responseChangeLogin.statusCode());
        assertEquals("gomer@test.ru", responseChangeLogin.body().jsonPath().getString("user.email"));
    }
    @Test
    @DisplayName("Change name without authorization")
    public void changeNameWithoutAuthorization() {
        userClient.createUser(user);
        user.setName("Gomer");
        Response responseChangeName = userClient.changeUserDataWithoutAuthorization(user);
        assertEquals(SC_UNAUTHORIZED, responseChangeName.statusCode());
        assertEquals("You should be authorised", responseChangeName.body().jsonPath().getString("message"));
    }
    @Test
    @DisplayName("Change login without authorization")
    public void userEditEmailWithoutAuthTest() {
        userClient.createUser(user);
        user.setEmail("gomer@test.ru");
        Response responseChangeLogin = userClient.changeUserDataWithoutAuthorization(user);
        assertEquals(SC_UNAUTHORIZED, responseChangeLogin.statusCode());
        assertEquals("You should be authorised", responseChangeLogin.body().jsonPath().getString("message"));
    }
    @Before
    public void setup() {
        user = getRandomUser();
    }
    @After
    public void delete() {
        if (token != null) {
            userClient.deleteUser(token);
        }
    }
}
