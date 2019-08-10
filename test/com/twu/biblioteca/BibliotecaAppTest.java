package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class BibliotecaAppTest {
    private BibliotecaApp bibliotecaApp;
    private PrintStream mockOut;
    private ArrayList<Book> testBooks = new ArrayList<Book>();

    @Before
    public void setUp() {
        //Given
        mockOut = mock(PrintStream.class);

        // Initialize some existing books in a library for test
        Book testBook1 = new Book("The Giver");
        Book testBook2 = new Book("The Secret Garden");
        testBooks.add(testBook1);
        testBooks.add(testBook2);

        bibliotecaApp = new BibliotecaApp(new Library(testBooks), mockOut);
    }

    @Test
    public void testShowWelcomeOnStarting() {
        final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
        // When
        bibliotecaApp.start();
        // Then
        verify(mockOut).println(WELCOME_MSG);
    }

    @Test
    public void testListAllBooks() {
        // When
        bibliotecaApp.listAllBooks();
        // Then
        for (Book testBook : testBooks) {
            verify(mockOut).println(testBook.toString());
        }
    }

    @Test
    public void testListBooksOnStarting() {
        // Given
        BibliotecaApp spyApp = spy(bibliotecaApp);
        // When
        spyApp.start();
        // Then
        verify(spyApp).listAllBooks();
    }
}