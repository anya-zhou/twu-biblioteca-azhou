package com.twu.biblioteca;

import java.util.ArrayList;

public class LoginService {
    private ArrayList<User> users = new ArrayList<>();

    public LoginService(ArrayList<User> users) {
        this.users = users;
    }

    public User login(String username, String password) {
        for (User user : this.users) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public User getUser(String username) {
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
