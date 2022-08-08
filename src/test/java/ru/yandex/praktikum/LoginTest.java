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

public class LoginTest {
    User user;
    String token;
    UserClient userClient = new UserClient();

    @Test
    @DisplayName("Login under an existing user")
    public void LoginWithExistingUser() {
        Response responseCreateUser = userClient.createUser(user);
        token = responseCreateUser.body().jsonPath().getString("accessToken");
        Response responseLoginExistingUser = userClient.loginUser(user, token);
        assertEquals(SC_OK, responseLoginExistingUser.statusCode());
    }
    @Test
    @DisplayName("Login with invalid login")
    public void LoginWithInvalidLogin() {
        Response responseCreate = userClient.createUser(user);
        token = responseCreate.body().jsonPath().getString("accessToken");
        user.setEmail("Invalid_login");
        Response responseInvalidLogin = userClient.loginUser(user, token);
        assertEquals(SC_UNAUTHORIZED, responseInvalidLogin.statusCode());
        assertEquals("email or password are incorrect", responseInvalidLogin.body().jsonPath().getString("message"));
    }
    @Test
    @DisplayName("Login with wrong password")
    public void loginWithWrongPassword() {
        Response responseCreate = userClient.createUser(user);
        token = responseCreate.body().jsonPath().getString("accessToken");
        user.setPassword("Wrong_password");
        Response responseIncorrectPassword = userClient.loginUser(user, token);
        assertEquals(SC_UNAUTHORIZED, responseIncorrectPassword.statusCode());
        assertEquals("email or password are incorrect", responseIncorrectPassword.body().jsonPath().getString("message"));
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
