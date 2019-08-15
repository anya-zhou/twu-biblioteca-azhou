package com.twu.biblioteca;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    // Gross simplification, plain text password is obviously NOT a good idea...
    public String getPassword() {
        return password;
    }
}
