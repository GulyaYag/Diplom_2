package ru.yandex.praktikum;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.UserResponse;
import ru.yandex.praktikum.model.User;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.praktikum.model.User.getRandomUser;
import static ru.yandex.praktikum.model.User.getRandomUserWithoutName;

public class CreateUserTest{
    User user;
    User userName;
    String token;
    UserClient userClient = new UserClient();
    @Test
    @DisplayName("Checking user creation")
    public void createUser() {
        Response responseCreateUser = userClient.createUser(user);
        UserResponse createUserResponse = responseCreateUser.as(UserResponse.class);
        token = responseCreateUser.body().jsonPath().getString("accessToken");
        assertEquals(SC_OK, responseCreateUser.statusCode());
        assertTrue(createUserResponse.success);
    }
    @Test
    @DisplayName("Create a user who is already registered")
    public void UserReRegistration() {
        Response responseCreationUser = userClient.createUser(user);
        token = responseCreationUser.body().jsonPath().getString("accessToken");
        Response responseCreationRepeatedUser = userClient.createUser(user);
        assertEquals(SC_FORBIDDEN, responseCreationRepeatedUser.statusCode());
        assertEquals("User already exists", responseCreationRepeatedUser.body().jsonPath().getString("message"));
    }
    @Test
    @DisplayName("Create a user and leave one of the required fields blank")
    public void createUserWithoutName() {
        userName = getRandomUserWithoutName();
        Response responseCreateUser = userClient.createUser(userName);
        assertEquals(SC_FORBIDDEN, responseCreateUser.statusCode());
        assertEquals("Email, password and name are required fields", responseCreateUser.body().jsonPath().getString("message"));
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
