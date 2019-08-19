package com.twu.biblioteca;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class UserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test(expected = IllegalArgumentException.class)
    @Parameters({
            "123456",   // no dash
            "123-456",  // wrong length
            "123-456a"  // not all digits
    })
    public void testInvalidUsernames(String username) {
        String password = "fakepassword";
        String name = "John Doe";
        String email = "jd@gmail.com";
        String phone = "0325218349";
        User validUser = new User(username, password, name, email, phone);
    }
}
