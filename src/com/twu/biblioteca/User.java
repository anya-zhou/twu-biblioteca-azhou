package com.twu.biblioteca;

public class User {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private final String INVALID_USERNAME_MESSAGE = "Library ID must be in the format of xxx-xxxx, "
                                                        + "where each 'x' is a numerical digit";

    public User(String username, String password, String name, String email, String phone)
            throws IllegalArgumentException{
        if (isValidUsername(username)) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
            this.phone = phone;
        } else {
            throw new IllegalArgumentException(INVALID_USERNAME_MESSAGE);
        }
    }

    public static boolean isValidUsername(String username) {
        return username.matches("\\d\\d\\d-\\d\\d\\d\\d");
    }

    public String getUsername() {
        return username;
    }

    // Gross simplification, plain text password is obviously NOT a good idea...
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass() == o.getClass() && this.getUsername().equals(((User) o).getUsername());
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }
}
