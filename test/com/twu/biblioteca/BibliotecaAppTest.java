package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class BibliotecaAppTest {
    private BibliotecaApp bibliotecaApp;
    private PrintStream mockOut;
    private ArrayList<Book> testBooks;

    @Before
    public void setUp() {
        //Given
        mockOut = mock(PrintStream.class);
        SampleAppData sampleData = new SampleAppData();

        // Populate library with some dummy books from SampleAppData
        bibliotecaApp = new BibliotecaApp(sampleData.getLibrary(), mockOut);
        testBooks = sampleData.getLibrary().getBooks();
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
    public void testPrintNothingForEmptyLibrary() {
        // Given - empty library as no library passed in through the constructor
        bibliotecaApp = new BibliotecaApp(mockOut);
        // When
        bibliotecaApp.listAllBooks();
        // Then
        verify(mockOut, times(0)).println(any());
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