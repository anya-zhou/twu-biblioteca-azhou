package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;

import java.io.PrintStream;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class BibliotecaAppTest {
    @Mock
    PrintStream mockOut;

    @Captor
    ArgumentCaptor<String> captor;

    private BibliotecaApp bibliotecaApp;
    private SampleAppData sampleData;

    @Before
    public void setUp() {
        bibliotecaApp = new BibliotecaApp(mockOut);
        sampleData = new SampleAppData();
    }

    @Test
    public void testShowWelcomeOnStarting() {
        // Given
        final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
        // When
        bibliotecaApp.start();
        // Then
        verify(mockOut).println(WELCOME_MSG);
    }

    @Test
    public void testListAllBooks() {
        // Given - populate library with some dummy books from SampleAppData
        bibliotecaApp = new BibliotecaApp(sampleData.getLibrary(), mockOut);
        ArrayList<Book> testBooks = sampleData.getLibrary().getBooks();

        // When
        bibliotecaApp.listAllBooks();

        // Then
        verify(mockOut, times(testBooks.size())).println(captor.capture());
        for (int i = 0; i < testBooks.size(); i++) {
            assertThat(captor.getAllValues().get(i), is(testBooks.get(i).toString()));
        }
    }

    @Test
    public void testPrintNothingForEmptyLibrary() {
        // Given - empty library as no library passed in through the constructor
        bibliotecaApp = new BibliotecaApp(mockOut);
        // When
        bibliotecaApp.listAllBooks();
        // Then
        verify(mockOut, never()).println(any());
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