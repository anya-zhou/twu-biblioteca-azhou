package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
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

    @Mock
    BufferedReader mockReader;

    @Captor
    ArgumentCaptor<String> captor;

    private BibliotecaApp bibliotecaApp;
    private SampleAppData sampleData;

    @Before
    public void setUp() throws IOException {
        // Given
        bibliotecaApp = new BibliotecaApp(mockOut, mockReader);
        sampleData = new SampleAppData();
        when(mockReader.readLine()).thenReturn("1"); // Default user input when prompted is always "1"
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
        bibliotecaApp = new BibliotecaApp(sampleData.getLibrary(), mockOut, mockReader);
        ArrayList<Book> testBooks = sampleData.getLibrary().getBooks();

        // When
        bibliotecaApp.listAllBooks();

        // Then
        verify(mockOut, times(testBooks.size() + 1)).println(captor.capture());

        for (int i = 0; i < testBooks.size() + 1; i++) {
            String outLine = captor.getAllValues().get(i);
            String[] bookDetails = outLine.split("\\s{2,}"); // Columns separated by 2+ spaces

            if (i == 0) {
                // Should print heading first
                assertThat(bookDetails[0], is("Title"));
                assertThat(bookDetails[1], is("Author"));
                assertThat(bookDetails[2], is("Year Published"));
            } else {
                // Each subsequent printed line should contain title, author and year
                Book bookToPrint = testBooks.get(i-1);
                assertThat(bookDetails[0], is(bookToPrint.getTitle()));
                assertThat(bookDetails[1], is(bookToPrint.getAuthorName()));
                assertThat(bookDetails[2], is(bookToPrint.getYearPublished()));
            }
        }
    }

    @Test
    public void testPrintNothingForEmptyLibrary() {
        // Given - empty library as no library passed in through the constructor
        bibliotecaApp = new BibliotecaApp(mockOut, mockReader);
        // When
        bibliotecaApp.listAllBooks();
        // Then
        verify(mockOut, never()).println(any());
    }

    @Test
    public void testShowMenu() {
        // When
        bibliotecaApp.showMenu();
        //Then
        verify(mockOut).println("1. List of books");
        verify(mockOut).println("Please enter the number of the option that you would like to select: ");
    }

    @Test
    public void testSelectListBookMenuOption() {
        // Given - mock selecting first option, see setUp
        BibliotecaApp spyApp = spy(bibliotecaApp);
        // When
        spyApp.executeUserSelectedOption("1");
        // Then
        verify(spyApp).listAllBooks();
    }

    @Test
    public void testShowMenuAndGetSelectionOnStarting() {
        // Given
        BibliotecaApp spyApp = spy(bibliotecaApp);
        // When
        spyApp.start();
        // Then
        verify(spyApp).showMenu();
        verify(spyApp).executeUserSelectedOption("1");
    }

    @Test
    public void testGetNotifiedOnInvalidOption() {
        // When
        bibliotecaApp.executeUserSelectedOption("100"); // valid option does not include 100
        // Then
        verify(mockOut).println("Please select a valid option!");
    }
}