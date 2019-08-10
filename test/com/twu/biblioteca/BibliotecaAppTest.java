package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;

public class BibliotecaAppTest {
    private BibliotecaApp bibliotecaApp;
    private PrintStream mockOut;

    @Before
    public void setUp() {
        mockOut = mock(PrintStream.class);
        bibliotecaApp = new BibliotecaApp(mockOut);
    }

    @Test
    public void testShowWelcomeOnStarting() {
        final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
        bibliotecaApp.start();
        verify(mockOut).println(WELCOME_MSG);
    }
}