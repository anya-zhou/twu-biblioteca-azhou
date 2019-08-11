package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
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

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    private BibliotecaApp bibliotecaApp;
    private SampleAppData sampleData;

    @Before
    public void setUp() throws IOException {
        // Given
        bibliotecaApp = new BibliotecaApp(mockOut, mockReader);
        sampleData = new SampleAppData();
        when(mockReader.readLine()).thenReturn(BibliotecaApp.LIST_BOOKS_KEY); // Default user input when prompted is always BibliotecaApp.LIST_BOOKS_KEY
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
        // Then - print header only
        verify(mockOut, times(1)).println(startsWith("Title"));
    }

    @Test
    public void testShowMenu() {
        // When
        bibliotecaApp.showMenu();
        //Then
        verify(mockOut).println(BibliotecaApp.LIST_BOOKS_KEY + ". List of books");
        verify(mockOut).println(BibliotecaApp.EXIT_APP_KEY + ". Exit the application");
        verify(mockOut).println("Please enter the number of the option that you would like to select: ");
    }

    @Test
    public void testSelectListBookMenuOption() {
        // Given - mock selecting first option, see setUp
        BibliotecaApp spyApp = spy(bibliotecaApp);
        // When
        spyApp.executeUserSelectedOption(BibliotecaApp.LIST_BOOKS_KEY);
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
        verify(spyApp).executeUserSelectedOption((String) any());
    }

    @Test
    public void testSelectListBooksOption() {
        // Given - user select list book option BibliotecaApp.LIST_BOOKS_KEY by default, see setUp
        BibliotecaApp spyApp = spy(bibliotecaApp);
        // When
        spyApp.executeUserSelectedOption(BibliotecaApp.LIST_BOOKS_KEY);
        // Then
        verify(spyApp).listAllBooks();
    }

    @Test
    public void testGetNotifiedOnInvalidOption() throws IOException {
        // Given - one invalid selection before a valid one
        when(mockReader.readLine()).thenReturn("100").thenReturn(BibliotecaApp.LIST_BOOKS_KEY);
        // When
        bibliotecaApp.start(); // valid option does not include 100
        // Then - should notify user and prompt user for input one more time
        verify(mockOut).println("Please select a valid option!");
        verify(mockReader, times(2)).readLine();
    }

    @Test
    public void testExitOption() throws IOException {
        // Given
        when(mockReader.readLine()).thenReturn(BibliotecaApp.EXIT_APP_KEY);
        exit.expectSystemExit(); // Prevents System.exit from existing the JVM
        // When
        bibliotecaApp.start();
        // Then - should not prompt for user input again
        verify(mockReader, times(1)).readLine();
        verifyNoMoreInteractions(mockReader);
    }
}