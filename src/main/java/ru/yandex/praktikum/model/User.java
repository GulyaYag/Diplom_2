package ru.yandex.praktikum.model;
import org.apache.commons.lang3.RandomStringUtils;
public class User {
    private String email;
    private String password;
    private String name;
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public static User getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(4, 8) + "@test.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password, name);
    }
    public static User getRandomUserWithoutName() {
        String email = RandomStringUtils.randomAlphabetic(4, 8) + "@test.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password);
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
