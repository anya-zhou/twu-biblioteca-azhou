package com.twu.biblioteca;

import org.junit.Test;

import java.util.ArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

public class LoginServiceTest {
    @Test
    public void testLoginValidDetails() {
        // Given
        String username = "123-4567";
        String password = "fakepassword";
        User validUser = new User(username, password);
        ArrayList<User> users = new ArrayList<User>();
        users.add(validUser);
        LoginService loginService = new LoginService(users);
        // When
        User loggedInUser = loginService.login(username, password);
        // Then
        assertThat(loggedInUser, is(validUser));
    }

    @Test
    public void testLoginWrongPassword() {
        // Given
        String username = "123-4567";
        String password = "fakepassword";
        User validUser = new User(username, password);
        ArrayList<User> users = new ArrayList<User>();
        users.add(validUser);
        LoginService loginService = new LoginService(users);
        // When
        User loggedInUser = loginService.login(username, "wrongpassword");
        // Then
        assertThat(loggedInUser, is(nullValue()));
    }

    @Test
    public void testLoginNonExistentUser() {
        // Given
        String username = "123-4567";
        String password = "fakepassword";
        User validUser = new User(username, password);
        ArrayList<User> users = new ArrayList<User>();
        users.add(validUser);
        LoginService loginService = new LoginService(users);
        // When
        User loggedInUser = loginService.login("765-4321", password);
        // Then
        assertThat(loggedInUser, is(nullValue()));
    }

}
