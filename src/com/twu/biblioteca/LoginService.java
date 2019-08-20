package com.twu.biblioteca;

import java.util.ArrayList;

class LoginService {
    private ArrayList<User> users = new ArrayList<>();

    LoginService(ArrayList<User> users) {
        this.users = users;
    }

    User login(String username, String password) {
        for (User user : this.users) {
            if (username.equals(user.getUsername()) && user.passwordIsMatching(password)) {
                return user;
            }
        }
        return null;
    }
}
